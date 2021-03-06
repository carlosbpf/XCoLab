package org.xcolab.view.pages.loginregister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xcolab.client.files.FilesClient;
import org.xcolab.client.files.pojo.FileEntry;
import org.xcolab.client.members.MembersClient;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.entity.utils.upload.FileUploadUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import javax.imageio.ImageIO;

public class ImageUploadUtils {

    private static final Logger _log = LoggerFactory.getLogger(ImageUploadUtils.class);

    private static final int IMAGE_RESIZE_WIDTH = 300;
    private static final int IMAGE_RESIZE_HEIGHT = 300;

    public static long uploadImage(URL url, String path) {
        try {
            BufferedImage image = ImageIO.read(url);
            byte[] imgBArr = FileUploadUtil.resizeAndCropImage(image, IMAGE_RESIZE_WIDTH, IMAGE_RESIZE_HEIGHT);


            FileEntry file = new FileEntry();
            file.setCreateDate(new Timestamp(new Date().getTime()));
            file.setFileEntryExtension("png");
            file.setFileEntrySize(imgBArr.length);
            file.setFileEntryName(url.toString());

            file = FilesClient.createFileEntry(file, imgBArr, path);

            return file.getFileEntryId();
        } catch (IOException  e) {
            _log.error("Could not upload picture {}", url.toString(), e);
        }

        return 0L;
    }

    public static void updateProfilePicture(String path, Member member, String picURL) {
        // Link picture if it is not yet set
        if (member.getPortraitId() == 0) {
            member.setPortraitFileEntryId(linkProfilePicture(path, picURL));
            MembersClient.updateMember(member);
        }
    }

    public static long linkProfilePicture(String path, String picUrl) {
        try {
            URL url = new URL(picUrl);
            return ImageUploadUtils.uploadImage(url, path);
        } catch (MalformedURLException e) {
            _log.warn("Could not upload  image with url {}", picUrl, e);
        }

        return 0L;
    }
}
