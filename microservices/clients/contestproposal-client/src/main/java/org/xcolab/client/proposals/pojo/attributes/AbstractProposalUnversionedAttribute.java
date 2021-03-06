package org.xcolab.client.proposals.pojo.attributes;

import java.sql.Timestamp;

class AbstractProposalUnversionedAttribute {

    private Long id_;
    private Long proposalid;
    private Long createauthorid;
    private Long lastauthorid;
    private Timestamp createdate;
    private Timestamp lastupdatedate;
    private String name;
    private Integer addtionalid;
    private Long numericvalue;
    private String stringvalue;
    private Double realvalue;

    public AbstractProposalUnversionedAttribute() {}

    public AbstractProposalUnversionedAttribute(AbstractProposalUnversionedAttribute value) {
        this.id_ = value.id_;
        this.proposalid = value.proposalid;
        this.createauthorid = value.createauthorid;
        this.lastauthorid = value.lastauthorid;
        this.createdate = value.createdate;
        this.lastupdatedate = value.lastupdatedate;
        this.name = value.name;
        this.addtionalid = value.addtionalid;
        this.numericvalue = value.numericvalue;
        this.stringvalue = value.stringvalue;
        this.realvalue = value.realvalue;
    }

    public AbstractProposalUnversionedAttribute(
            Long id_,
            Long proposalid,
            Long createauthorid,
            Long lastauthorid,
            Timestamp createdate,
            Timestamp lastupdatedate,
            String name,
            Integer addtionalid,
            Long numericvalue,
            String stringvalue,
            Double realvalue
    ) {
        this.id_ = id_;
        this.proposalid = proposalid;
        this.createauthorid = createauthorid;
        this.lastauthorid = lastauthorid;
        this.createdate = createdate;
        this.lastupdatedate = lastupdatedate;
        this.name = name;
        this.addtionalid = addtionalid;
        this.numericvalue = numericvalue;
        this.stringvalue = stringvalue;
        this.realvalue = realvalue;
    }

    public Long getId_() {
        return this.id_;
    }

    public void setId_(Long id_) {
        this.id_ = id_;
    }

    public Long getProposalId() {
        return this.proposalid;
    }

    public void setProposalId(Long proposalid) {
        this.proposalid = proposalid;
    }

    public Long getCreateAuthorId() {
        return this.createauthorid;
    }

    public void setCreateAuthorId(Long createauthorid) {
        this.createauthorid = createauthorid;
    }

    public Long getLastAuthorId() {
        return this.lastauthorid;
    }

    public void setLastAuthorId(Long lastauthorid) {
        this.lastauthorid = lastauthorid;
    }

    public Timestamp getCreateDate() {
        return this.createdate;
    }

    public void setCreateDate(Timestamp createdate) {
        this.createdate = createdate;
    }

    public Timestamp getLastUpdateDate() {
        return this.lastupdatedate;
    }

    public void setLastUpdateDate(Timestamp lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAddtionalId() {
        return this.addtionalid;
    }

    public void setAddtionalId(Integer addtionalid) {
        this.addtionalid = addtionalid;
    }

    public Long getNumericValue() {
        return this.numericvalue;
    }

    public void setNumericValue(Long numericvalue) {
        this.numericvalue = numericvalue;
    }

    public String getStringValue() {
        return this.stringvalue;
    }

    public void setStringValue(String stringvalue) {
        this.stringvalue = stringvalue;
    }

    public Double getRealValue() {
        return this.realvalue;
    }

    public void setRealValue(Double realvalue) {
        this.realvalue = realvalue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id_ == null) ? 0 : id_.hashCode());
        result = prime * result + ((proposalid == null) ? 0 : proposalid.hashCode());
        result = prime * result + ((createauthorid == null) ? 0 : createauthorid.hashCode());
        result = prime * result + ((lastauthorid == null) ? 0 : lastauthorid.hashCode());
        result = prime * result + ((createdate == null) ? 0 : createdate.hashCode());
        result = prime * result + ((lastupdatedate == null) ? 0 : lastupdatedate.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((addtionalid == null) ? 0 : addtionalid.hashCode());
        result = prime * result + ((numericvalue == null) ? 0 : numericvalue.hashCode());
        result = prime * result + ((stringvalue == null) ? 0 : stringvalue.hashCode());
        result = prime * result + ((realvalue == null) ? 0 : realvalue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractProposalUnversionedAttribute other = (AbstractProposalUnversionedAttribute) obj;
        if (id_ == null) {
            if (other.id_ != null) {
                return false;
            }
        } else if (!id_.equals(other.id_)) {
            return false;
        }
        if (proposalid == null) {
            if (other.proposalid != null) {
                return false;
            }
        } else if (!proposalid.equals(other.proposalid)) {
            return false;
        }
        if (createauthorid == null) {
            if (other.createauthorid != null) {
                return false;
            }
        } else if (!createauthorid.equals(other.createauthorid)) {
            return false;
        }
        if (lastauthorid == null) {
            if (other.lastauthorid != null) {
                return false;
            }
        } else if (!lastauthorid.equals(other.lastauthorid)) {
            return false;
        }
        if (createdate == null) {
            if (other.createdate != null) {
                return false;
            }
        } else if (!createdate.equals(other.createdate)) {
            return false;
        }
        if (lastupdatedate == null) {
            if (other.lastupdatedate != null) {
                return false;
            }
        } else if (!lastupdatedate.equals(other.lastupdatedate)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (addtionalid == null) {
            if (other.addtionalid != null) {
                return false;
            }
        } else if (!addtionalid.equals(other.addtionalid)) {
            return false;
        }
        if (numericvalue == null) {
            if (other.numericvalue != null) {
                return false;
            }
        } else if (!numericvalue.equals(other.numericvalue)) {
            return false;
        }
        if (stringvalue == null) {
            if (other.stringvalue != null) {
                return false;
            }
        } else if (!stringvalue.equals(other.stringvalue)) {
            return false;
        }
        if (realvalue == null) {
            if (other.realvalue != null) {
                return false;
            }
        } else if (!realvalue.equals(other.realvalue)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ProposalUnversionedAttribute (");

        sb.append(id_);
        sb.append(", ").append(proposalid);
        sb.append(", ").append(createauthorid);
        sb.append(", ").append(lastauthorid);
        sb.append(", ").append(createdate);
        sb.append(", ").append(lastupdatedate);
        sb.append(", ").append(name);
        sb.append(", ").append(addtionalid);
        sb.append(", ").append(numericvalue);
        sb.append(", ").append(stringvalue);
        sb.append(", ").append(realvalue);

        sb.append(")");
        return sb.toString();
    }
}
