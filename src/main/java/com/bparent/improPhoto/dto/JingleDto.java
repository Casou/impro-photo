package com.bparent.improPhoto.dto;

import com.bparent.improPhoto.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JingleDto {

    private BigInteger id;
    private String name;
    private String fileName;
    private String folder;
    private String path;

    public String getPath() {
        return FileUtils.formatPathWithCharacter("/handler/jingles/" + this.folder + "/" + this.fileName, "/");
    }

}
