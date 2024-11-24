package com.ub.org.demo.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;





import com.ub.org.demo.service.DocumentoService;

@RestController
public class DocumentoController {

      @Autowired
    private DocumentoService documentoService;
    
    private static final String DIRETORIO_ARQUIVOS = "C://Users//millena.ti//OneDrive - União Brasil//documentos_filiados//"; 
     // Endpoint para upload de um novo documento
     @PostMapping("/upload/{titulo}")
        public ResponseEntity<String> uploadDocumento(
            @PathVariable String titulo, // Captura o ID do filiado
            @RequestParam(required = false) MultipartFile foto,
            @RequestParam(required = false) MultipartFile cpf,
            @RequestParam(required = false) MultipartFile tituloDoc,
            @RequestParam(required = false) MultipartFile ficha) {

        try {
            // Envie os arquivos para o serviço que cuidará do armazenamento
            documentoService.salvarDocumento(titulo, foto, cpf, tituloDoc, ficha);
           
            return ResponseEntity.ok("Documentos enviados com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao enviar documentos: " + e.getMessage());
        }
    }
 

      // Endpoint para listar todos os arquivos de um diretório específico
      @GetMapping("/documentos/{titulo}/arquivos")
      public ResponseEntity<List<String>> listarArquivos(@PathVariable String titulo) {
        File pastaFiliado = new File(DIRETORIO_ARQUIVOS + titulo); // Diretório com os arquivos
        List<String> arquivos = new ArrayList<>();
        
        // Verifica se o diretório existe
        if (pastaFiliado.exists() && pastaFiliado.isDirectory()) {
            for (File arquivo : pastaFiliado.listFiles()) {
                if (arquivo.isFile()) {
                    arquivos.add(arquivo.getName()); // Adiciona o nome do arquivo à lista
                }
            }
        }
    
        // Retorna a lista de arquivos para o cliente
        return ResponseEntity.ok(arquivos);
    }

    // Endpoint para servir os arquivos de imagem dinamicamente
    @GetMapping("/documentos/{titulo}/imagem/{fileName}") 
    public ResponseEntity<Resource> serveImage(@PathVariable String titulo, @PathVariable String fileName) {
        File file = new File(DIRETORIO_ARQUIVOS + titulo + "//" + fileName);
        
        if (file.exists()) {
            // Detectando o tipo MIME do arquivo com base no nome do arquivo
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            
            // Se não conseguir detectar, vamos configurar como imagem JPEG por padrão
            if (mimeType == null || !mimeType.startsWith("image")) {
                mimeType = "image/jpeg"; // Caso o tipo não seja identificado, tratamos como JPEG
            }

            // Usando o tipo MIME detectado
            MediaType mediaType = MediaType.parseMediaType(mimeType);

            // Criando o recurso a partir do arquivo
            Resource resource = new FileSystemResource(file);

            // Retorna a imagem com o tipo MIME correto para exibição
            return ResponseEntity.ok()
                    .contentType(mediaType)  // Define o tipo de mídia corretamente
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 se o arquivo não for encontrado
        }
    }


}
