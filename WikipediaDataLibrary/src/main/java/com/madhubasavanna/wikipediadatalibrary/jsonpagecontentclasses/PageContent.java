
package com.madhubasavanna.wikipediadatalibrary.jsonpagecontentclasses;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "pageid",
    "ns",
    "title",
    "extract"
})
public class PageContent {

    @JsonProperty("pageid")
    private Integer pageid;
    @JsonProperty("ns")
    private Integer ns;
    @JsonProperty("title")
    private String title;
    @JsonProperty("extract")
    private String extract;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pageid")
    public Integer getPageid() {
        return pageid;
    }

    @JsonProperty("pageid")
    public void setPageid(Integer pageid) {
        this.pageid = pageid;
    }

    @JsonProperty("ns")
    public Integer getNs() {
        return ns;
    }

    @JsonProperty("ns")
    public void setNs(Integer ns) {
        this.ns = ns;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("extract")
    public String getExtract() {
        return extract;
    }

    @JsonProperty("extract")
    public void setExtract(String extract) {
        this.extract = extract;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
