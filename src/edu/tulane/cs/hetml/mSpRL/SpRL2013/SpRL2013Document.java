package edu.tulane.cs.hetml.mSpRL.SpRL2013;

import edu.tulane.cs.hetml.mSpRL.SpRLXmlDocument;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "url",
        "cp",
        "text",
        "tags"
})
@XmlRootElement(name = "SpRL")
public class SpRL2013Document implements SpRLXmlDocument {

    @XmlElement(name = "URL", required = false)
    protected URL url;
    @XmlElement(name = "CP", required = false)
    protected CP cp;
    @XmlElement(name = "TEXT", required = true)
    protected TEXT text;
    @XmlElement(name = "TAGS", required = true)
    protected TAGS tags;

    @XmlTransient
    protected String filename;


    /**
     * Gets the value of the url property.
     *
     * @return possible object is
     * {@link URL }
     */
    public URL getURL() {
        return url;
    }

    /**
     * Sets the value of the url property.
     *
     * @param value allowed object is
     *              {@link URL }
     */
    public void setURL(URL value) {
        this.url = value;
    }

    /**
     * Gets the value of the cp property.
     *
     * @return possible object is
     * {@link CP }
     */
    public CP getCP() {
        return cp;
    }

    /**
     * Sets the value of the cp property.
     *
     * @param value allowed object is
     *              {@link CP }
     */
    public void setCP(CP value) {
        this.cp = value;
    }

    /**
     * Gets the value of the text property.
     *
     * @return possible object is
     * {@link TEXT }
     */
    public TEXT getTEXT() {
        return text;
    }

    /**
     * Sets the value of the text property.
     *
     * @param value allowed object is
     *              {@link TEXT }
     */
    public void setTEXT(TEXT value) {
        this.text = value;
    }

    /**
     * Gets the value of the tags property.
     *
     * @return possible object is
     * {@link TAGS }
     */
    public TAGS getTAGS() {
        return tags;
    }

    /**
     * Sets the value of the tags property.
     *
     * @param value allowed object is
     *              {@link TAGS }
     */
    public void setTAGS(TAGS value) {
        this.tags = value;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

}
