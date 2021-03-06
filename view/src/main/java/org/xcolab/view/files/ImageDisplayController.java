package org.xcolab.view.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.xcolab.client.admin.enums.ConfigurationAttributeKey;
import org.xcolab.client.files.FilesClient;
import org.xcolab.client.files.exceptions.FileEntryNotFoundException;
import org.xcolab.client.files.pojo.FileEntry;
import org.xcolab.client.members.MembersClient;
import org.xcolab.client.members.exceptions.MemberNotFoundException;
import org.xcolab.client.members.pojo.Member;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@PropertySource({"file:${user.home}/.xcolab.application.properties"})
public class ImageDisplayController {

    @Autowired
    private Environment env;

    @GetMapping({"/image/{whatever}"})
    public void serveImage(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String imageId = null;

        if (request.getParameter("img_id") != null) {
            imageId = request.getParameter("img_id");
        }

        if (request.getParameter("portraitId") != null) {
            if (request.getParameter("userId") != null) {
                try {
                    Member member = MembersClient.getMember(Long.parseLong(request.getParameter("userId")));
                    if (member.getPortraitFileEntryId() != null) {
                        imageId = member.getPortraitFileEntryId() + "";
                    } else {
                        imageId = null;
                    }
                } catch (MemberNotFoundException e) {
                    imageId = request.getParameter("portraitId");
                }
            } else {
                imageId = request.getParameter("portraitId");
            }
        }

        String path = request.getSession().getServletContext().getRealPath("/");
        String fileUploadPath = env.getProperty("files.upload.dir");

        path = (fileUploadPath!=null)?(fileUploadPath):(path);
        if (imageId != null && !imageId.isEmpty()) {
            try {
                FileEntry fileEntry = FilesClient.getFileEntry(new Long(imageId));
                String filePath = FilesClient.getFilePathFromFinalDestination(fileEntry, path);
                if (filePath != null) {
                    //check if file exists
                    File f = new File(filePath);
                    if(f.exists() && !f.isDirectory()) {
                        sendImageToResponse(request, response, filePath);
                        return;
                    }else{
                        String environmentStatus = env.getProperty("environment");
                        if(!environmentStatus.equals("production")){
                            response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                            String newURL = ConfigurationAttributeKey.COLAB_URL.get() + "/"+ request.getRequestURI()+"?"+ request.getQueryString();
                            response.setHeader("Location", newURL);
                            return;
                        }
                    }
                }
            } catch (FileEntryNotFoundException ignored) {

            }
        }

        if (request.getRequestURI().contains("user_male_portrait")) {

            String pathToFailOverImage = ConfigurationAttributeKey
                    .ACTIVE_THEME.get().getImagePath() + "/user_default.png";
            response.sendRedirect(pathToFailOverImage);
            return;
        }
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException ignored) {

        }
    }

    private void sendImageToResponse(HttpServletRequest request, HttpServletResponse response, String filePath) {

        try {
            ServletContext servletContext = request.getSession().getServletContext();
            String mime = servletContext.getMimeType(filePath);
            if (mime == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            response.setContentType(mime);
            File file = new File(filePath);
            response.setContentLength((int) file.length());

            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
            in.close();
            return;
        } catch (IOException ignored) {

        }
        try{
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }catch (IOException ignored){

        }
    }
}
