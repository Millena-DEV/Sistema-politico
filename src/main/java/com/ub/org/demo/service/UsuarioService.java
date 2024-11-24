package com.ub.org.demo.service;

import com.ub.org.demo.enums.Funcao;
import com.ub.org.demo.repository.UsuarioRepository;
import com.ub.org.demo.view.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuariosRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }

        // Supondo que usuario.getFuncao() retorna uma String ou Enum que pode ser convertido
          
        String role = usuario.getFuncao().name(); // Ex: "ADMIN" ou "USER"

        return org.springframework.security.core.userdetails.User.withUsername(usuario.getEmail())
            .password(usuario.getSenha())
            .roles(role) // Assegure-se de que não tem o prefixo "ROLE_"
            .build();
    }

    public void salvarUsuario(String email, String senha, String nome, String sobrenome, String cargo,Funcao funcao,String uf,String municipio ) {
        String senhaCriptografada = passwordEncoder.encode(senha);
        Usuarios usuario = new Usuarios();
        usuario.setEmail(email);
        usuario.setSenha(senhaCriptografada);
        usuario.setNome(nome);
        usuario.setSobrenome(sobrenome);
        usuario.setCargo(cargo);
        usuario.setFuncao(funcao);   // Aqui, funcao seria uma String que corresponde a um valor do Enum
        usuario.setUf(uf);
        usuario.setMunicipio(municipio);
        // Preencha outros campos conforme necessário
    
        usuariosRepository.save(usuario);
    }
    


    public boolean authenticate(String email, String rawPassword) {
        Usuarios usuario = usuariosRepository.findByEmail(email);
        if (usuario != null) {
            return passwordEncoder.matches(rawPassword, usuario.getSenha());
        }
        return false;
    }

    public void userLogado(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getUsername(); // Usando email como nome de usuário
            Usuarios usuario = usuariosRepository.findByEmail(email); // Busca o usuário pelo email
            
    
            if (usuario != null) {
                // Adiciona todas as informações do usuário ao modelo
                model.addAttribute("nome", usuario.getNome()); // Adiciona o objeto de usuário ao modelo
                model.addAttribute("sobrenome", usuario.getSobrenome());
                model.addAttribute("cargo", usuario.getCargo());
                model.addAttribute("funcao", usuario.getFuncao());
                model.addAttribute("uf", usuario.getUf());
                model.addAttribute("municipio", usuario.getMunicipio());
                model.addAttribute("email", usuario.getEmail());
                model.addAttribute("senha", usuario.getSenha());
          
                // Adicione outras propriedades conforme necessário
                // Exemplo:
                // model.addAttribute("email", user.getEmail());
                // model.addAttribute("telefone", user.getTelefone());
            }
        }
    }

   
}
