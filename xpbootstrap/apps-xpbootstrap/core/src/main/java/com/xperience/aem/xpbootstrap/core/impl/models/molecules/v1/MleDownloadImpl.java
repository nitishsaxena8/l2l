package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.commons.DownloadResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.designer.Style;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xperience.aem.xpbootstrap.core.component.Utils.Link;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleDownload;
import com.xperience.aem.xpbootstrap.core.servlet.DownloadServlet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.eris.notnull.NotNull;
import se.eris.notnull.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.Calendar;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = {MleDownload.class, ComponentExporter.class},
        resourceType = {MleDownloadImpl.RESOURCE_TYPE_V1})
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class MleDownloadImpl extends AbstractComponentImpl implements MleDownload {

    private static final Logger LOGGER = LoggerFactory.getLogger(MleDownloadImpl.class);

    public final static String RESOURCE_TYPE_V1 = "xpbootstrap/components/master/organisms/download/v1/download";


    @Self
    SlingHttpServletRequest request;

    @Self
    LinkManager linkManager;

    @ScriptVariable
    private Resource resource;

    @OSGiService
    private MimeTypeService mimeTypeService;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @JsonIgnore
    @Nullable
    protected Style currentStyle;

    @SlingObject
    ResourceResolver resourceResolver;

    Link link;


    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String atmTitleLevel;


    private boolean displaySize;

    private boolean displayFormat;


    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String atmTitleText;


    public String getActionText() {
        return actionText;
    }

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String actionText;

    private String filename;

    private String format;

    private String size;

    private String extension;


    @PostConstruct
    protected void initModel() {
        String fileReference = properties.get(DownloadResource.PN_REFERENCE, String.class);
        if (currentStyle != null) {
            displaySize = currentStyle.get(PN_DISPLAY_SIZE, true);
            displayFormat = currentStyle.get(PN_DISPLAY_FORMAT, true);

        }
        if (StringUtils.isNotBlank(fileReference)) {
            initAssetDownload(fileReference);
        } else {
            Resource file = resource.getChild(DownloadResource.NN_FILE);
            if (file != null) {
                initFileDownload(file);
            }
        }
    }

    private void initFileDownload(Resource file) {
        filename = properties.get(DownloadResource.PN_FILE_NAME, String.class);
        if (StringUtils.isNotEmpty(filename)) {
            Resource fileContent = file.getChild(JcrConstants.JCR_CONTENT);
            if (fileContent != null) {
                ValueMap valueMap = fileContent.adaptTo(ValueMap.class);
                if (valueMap != null) {
                    format = valueMap.get(JcrConstants.JCR_MIMETYPE, String.class);

                    if (StringUtils.isNotEmpty(format)) {
                        extension = mimeTypeService.getExtension(format);
                    }
                   // Calendar calendar = valueMap.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);

                    link = linkManager.get(getDownloadUrl(file) + "/" + filename).build();
                    size = FileUtils.byteCountToDisplaySize(getFileSize(fileContent));
                }
            }
        }
    }

    private void initAssetDownload(String fileReference) {
        Resource downloadResource = resourceResolver.getResource(fileReference);
        if (downloadResource != null) {
            Asset downloadAsset = downloadResource.adaptTo(Asset.class);
            if (downloadAsset != null) {
               // Calendar resourceLastModified = properties.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);

                filename = downloadAsset.getName();

                format = downloadAsset.getMetadataValue(DamConstants.DC_FORMAT);

                if (StringUtils.isNotEmpty(format)) {
                    extension = mimeTypeService.getExtension(format);
                }

                if (StringUtils.isEmpty(extension)) {
                    extension = FilenameUtils.getExtension(filename);
                }

                link = linkManager.get(getDownloadUrl(downloadResource)).build();


                long rawFileSize;
                Object rawFileSizeObject = downloadAsset.getMetadata(DamConstants.DAM_SIZE);

                if (rawFileSizeObject != null) {
                    rawFileSize = (Long) rawFileSizeObject;
                } else {
                    rawFileSize = downloadAsset.getOriginal().getSize();
                }

                size = FileUtils.byteCountToDisplaySize(rawFileSize);
            }
        }
    }

    @Nonnull
    public String getExportedType() {
        return request.getResource().getResourceType();
    }

    @Override
    public String getUrl() {
        return (link != null) ? link.getURL() : null;
    }


    @Override
    public String getTitle() {
        return atmTitleText;
    }


    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public String getSize() {
        return size;
    }

    @Override
    public boolean displaySize() {
        return displaySize;
    }

    @Override
    public boolean displayFormat() {
        return displayFormat;
    }


    @Override
    public String getExtension() {
        return extension;
    }

    private long getFileSize(Resource resource) {
        long size = 0;
        Node node = resource.adaptTo(Node.class);
        if (node != null) {
            try {
                Property data = node.getProperty(JcrConstants.JCR_DATA);
                size = data.getBinary().getSize();
            } catch (RepositoryException ex) {
                LOGGER.error("Unable to detect binary file size for " + resource.getPath(), ex);
            }
        }
        return size;
    }

    @NotNull
    private String getDownloadUrl(Resource resource) {
        StringBuilder downloadUrlBuilder = new StringBuilder();
        downloadUrlBuilder.append(resource.getPath())
                .append(".")
                .append(DownloadServlet.SELECTOR)
                .append(".");
        downloadUrlBuilder.append(extension);
        return downloadUrlBuilder.toString();
    }


    @Override
    public String getAtmTitleText() {
        return atmTitleText;
    }

    @Override
    public String getAtmTitleLevel() {
        return atmTitleLevel;
    }

    @Override
    public String getIcon()
    {
        String fileType = getExtension();
        switch (fileType){
            case "pdf": {
                return "bi bi-file-earmark-pdf-fill";
            }
            case "ppt": {
                return "bi bi-filetype-ppt";
            }
            case "txt": {
                return "bi bi-filetype-docx";
            }
            case "zip": {
                return "bi bi-file-earmark-zip";
            }
            case "mp4": {
                return "bi bi-filetype-mp4";
            }
            case "mp3": {
                return "bi bi-filetype-mp3";
            }
            case "xlsx":{
                return "bi bi-file-earmark-excel";
            }
            default:{
                return "bi bi-files";
            }
        }
    }
}
