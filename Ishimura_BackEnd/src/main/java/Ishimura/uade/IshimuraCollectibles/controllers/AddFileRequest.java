package Ishimura.uade.IshimuraCollectibles.controllers;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddFileRequest {
    private Long idColeccionable;
    private MultipartFile file;
}
