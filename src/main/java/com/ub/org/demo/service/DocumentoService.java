package com.ub.org.demo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ub.org.demo.repository.DocumentoRepository;
import com.ub.org.demo.repository.FiliadoViewRepository;
import com.ub.org.demo.view.Documentos;
import com.ub.org.demo.view.FiliadoView;
@Service
public class DocumentoService {
  private static final String DIRETORIO_ARQUIVOS = "C://Users//millena.ti//OneDrive - União Brasil//documentos_filiados//";  // Defina o caminho do diretório onde os arquivos serão armazenados

  @Autowired
  private DocumentoRepository documentoRepository;
  @Autowired
  private FiliadoViewRepository filiadoRepository;

// Método para salvar os documentos (foto, cpf, título e ficha) de um filiado
public void salvarDocumento(String titulo, MultipartFile foto, MultipartFile cpf, MultipartFile tituloDoc, MultipartFile ficha) throws IOException {
    // Criação do diretório específico para o filiado (usando o título como nome da pasta)
    File pastaFiliado = new File(DIRETORIO_ARQUIVOS + titulo); // Nome da pasta será o título do filiado
    
    // Verifica se a pasta do filiado já existe, caso contrário, cria
    if (!pastaFiliado.exists()) {
        pastaFiliado.mkdirs();
    }
    System.out.println("parametro: " + titulo);
    System.out.println("foto: " + foto);
    System.out.println("cpf: " + cpf);
    System.out.println("tituloDoc: " + tituloDoc);
    System.out.println("ficha: " + ficha);

      salvarArquivo(ficha, pastaFiliado, titulo, titulo);
    // Salvar cada arquivo e associá-lo ao filiado
    if (foto != null && !foto.isEmpty()) {
        salvarArquivo(foto, pastaFiliado, "foto", titulo); // Passa o 'titulo' para associar corretamente
    }

    if (cpf != null && !cpf.isEmpty()) {
        salvarArquivo(cpf, pastaFiliado, "cpf", titulo);
    }

    if (tituloDoc != null && !tituloDoc.isEmpty()) {
        salvarArquivo(tituloDoc, pastaFiliado, "titulo", titulo);
    }

    if (ficha != null && !ficha.isEmpty()) {
        salvarArquivo(ficha, pastaFiliado, "ficha", titulo);
    }
   
}

// Método para salvar o arquivo no diretório do filiado
private void salvarArquivo(MultipartFile arquivo, File diretório, String tipoDocumento, String titulo) throws IOException {
    // Gerar um nome único para o arquivo (incluindo o título do filiado e UUID)
    if (arquivo == null || arquivo.isEmpty()) {
        // Ignorar o arquivo se for null ou vazio, ou lançar uma exceção, se necessário
        return;  // Ou lançar uma exceção personalizada se necessário
    }
      // Adicionar log para verificar o valor do titulo
      System.out.println("Título recebido: " + titulo);
    String nomeArquivoOriginal = arquivo.getOriginalFilename();
    String extensaoArquivo = nomeArquivoOriginal.substring(nomeArquivoOriginal.lastIndexOf("."));
    
    // Gerar um nome único para o arquivo, incluindo o título e UUID
    String nomeArquivo = titulo + "_" + tipoDocumento + "_" + UUID.randomUUID().toString() + extensaoArquivo;

    // Caminho completo onde o arquivo será salvo
    Path caminhoArquivo = Paths.get(diretório.getAbsolutePath() + "/" + nomeArquivo);

    // Copiar o arquivo para o diretório
    try (InputStream inputStream = arquivo.getInputStream()) {
        Files.copy(inputStream, caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);
    }

    // Buscar o filiado pelo título
    System.out.println("Procurando filiado com título: " + titulo); // Para verificar o título
    FiliadoView filiado = filiadoRepository.findByTitulo(titulo)
            .orElseThrow(() -> new IllegalArgumentException("Filiado não encontrado com título: " + titulo));
            System.out.println("Procurando filiado com título: " + titulo); // Para verificar o título

     
    // Criar um novo objeto Documento para persistir no banco de dados
    Documentos documento = new Documentos();
    documento.setFiliado(filiado); // Associa o filiado ao documento
    documento.setNomeArquivo(nomeArquivo);
    documento.setTipoMime(arquivo.getContentType());
    documento.setCaminhoArquivo(caminhoArquivo.toString());

    // Persistir o documento no banco de dados
    documentoRepository.save(documento);
}
public Optional<FiliadoView> buscarPorId(String titulo) {
    return filiadoRepository.findByTitulo(titulo);
}


  // Método para listar documentos de um filiado
  public List<Documentos> listarDocumentos(FiliadoView filiado) {
      return documentoRepository.findByFiliado(filiado);
  }

  // Método para buscar um documento por ID
  public Optional<Documentos> buscarDocumentoPorId(Long idDocumento) {
      return documentoRepository.findByIdDocumento(idDocumento);
  }
}
