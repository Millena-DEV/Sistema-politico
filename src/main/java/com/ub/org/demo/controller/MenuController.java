package com.ub.org.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import com.ub.org.demo.service.CandidatoService;
import com.ub.org.demo.service.FiliadoService;
import com.ub.org.demo.service.UsuarioService;
import com.ub.org.demo.view.Usuarios;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller // Certifique-se de que o controlador esteja anotado com @Controller
public class MenuController { // Renomeado para começar com letra maiúscula


    @Autowired
    private FiliadoService filiadoService;
    @Autowired
    private CandidatoService candidatoService;
    @Autowired
    private UsuarioService  userService;

    @PostMapping("/login")
        public String login(@RequestParam String username, @RequestParam String password, Model model) {
            if (userService.authenticate(username, password)) {
                return "redirect:/index"; // Redirecionar para a página inicial ou painel
            }
            model.addAttribute("error", true);
            return "login"; // Retorna à página de login em caso de falha
}
        @GetMapping("/login")
        public String showLoginForm(Model model) {
            return "login"; // Retorna a página de cadastro
        }
        
        @GetMapping("/documentos")
        public String showDocumentoForm(Model model) {
            return "documentos"; // Retorna a página de cadastro
        }
        @GetMapping("/redefinir-senha")
        public String showRedefinirSenha(@RequestParam String token, Model model) {
            // Adicionando o token ao modelo
            model.addAttribute("token", token);
            return "redefinir-senha";  // Nome da view (por exemplo, redefinir-senha.html)
        }

    @GetMapping("/cadastrar")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuarios());
        return "cadastrar"; // Retorna a página de cadastro
    }

    @PostMapping("/cadastrar")
     public String registerUser(@Valid @ModelAttribute("usuario") Usuarios user, BindingResult result) {
    if (result.hasErrors()) {
        return "cadastrar"; // Retorna ao formulário se houver erros
    }
    
    // Passa os parâmetros individuais para o serviço
    userService.salvarUsuario(
        user.getEmail(),
        user.getSenha(),
        user.getNome(),
        user.getSobrenome(),
        user.getCargo(),
        user.getFuncao(),
        user.getUf(),
        user.getMunicipio()
    );
    
    // Redireciona para a página de login após o cadastro bem-sucedido
    return "redirect:/login";
}

    
    @GetMapping("/index")
    public String mostrarIndex(Model model,  @RequestParam(required = false) String partido,
    @RequestParam(required = false) Integer ano) {
        long totalFiliados = filiadoService.contarFiliados();
          // Definindo valores padrão se não forem fornecidos
    if (partido == null) {
        partido = "UNIÃO"; // Valor padrão
    }
    if (ano == null) {
        ano = 2024; // Valor padrão
    }

        // Definir valores padrão
        String[] partidos = {
            "UNIÃO",
            "AGIR",
            "AVANTE",
            "CIDADANIA",
            "DC",
            "DEM",
            "MDB",
            "MOBILIZA",
            "NOVO",
            "PATRIOTA",
            "PC do B",
            "PCB",
            "PDT",
            "PHS",
            "PL",
            "PMB",
            "PMDB",
            "PMN",
            "PODE",
            "PP",
            "PPL",
            "PPS",
            "PR",
            "PRB",
            "PRD",
            "PROS",
            "PRP",
            "PRTB",
            "PSB",
            "PSC",
            "PSD",
            "PSDB",
            "PSDC",
            "PSL",
            "PSOL",
            "PT",
            "PT do B",
            "PTB",
            "PTC",
            "PTN",
            "PV",
            "REDE",
            "REPUBLICANOS",
            "SD",
            "SOLIDARIEDADE"
        };
        String[] anos = {
            "2024",
            "2022",
            "2020",
            "2018",
            "2016",
        };
        // Cargos que você deseja contar
        String[] cargos = {
            "DEPUTADO DISTRITAL",
            "DEPUTADO ESTADUAL",
            "DEPUTADO FEDERAL",
            "GOVERNADOR",
            "SENADOR",
            "VICE-GOVERNADOR",
            "PREFEITO",
            "VEREADOR",
            "VICE-PREFEITO",
            "PRESIDENTE",
            "VICE-PRESIDENTE"
        };
        
        // Lista para armazenar contagens de candidatos
        List<Long> contagemCandidatos = new java.util.ArrayList<>();
        Map<String, Long> contagemPorCargo = new HashMap<>();

        // Contagem de candidatos com base nos filtros
        for (String cargo : cargos) {
            long contagem = candidatoService.contarCandidatosPorPartidoECargo(partido, cargo, ano);
            System.out.println("Cargo: " + cargo + ", Contagem: " + contagem); // Debug
            contagemCandidatos.add(contagem);
            contagemPorCargo.put(cargo, contagem);
        }
        

        // Exibe todos os cargos e suas contagens
        for (Map.Entry<String, Long> entry : contagemPorCargo.entrySet()) {
            System.out.println("Cargo: " + entry.getKey() + ", Contagem de Candidatos: " + entry.getValue());
        }

        // Adiciona atributos ao modelo
        model.addAttribute("totalFiliados", totalFiliados);

        // Presumindo que os cargos estejam na mesma ordem da lista contagemCandidatos
        model.addAttribute("totalDeputadoDistrital", contagemPorCargo.get("DEPUTADO DISTRITAL"));
        model.addAttribute("totalDeputadoEstadual", contagemPorCargo.get("DEPUTADO ESTADUAL"));
        model.addAttribute("totalDeputadoFederal", contagemPorCargo.get("DEPUTADO FEDERAL"));
        model.addAttribute("totalGovernador", contagemPorCargo.get("GOVERNADOR"));
        model.addAttribute("totalSenador", contagemPorCargo.get("SENADOR"));
        model.addAttribute("totalViceGovernador", contagemPorCargo.get("VICE-GOVERNADOR"));
        model.addAttribute("totalPrefeitos", contagemPorCargo.get("PREFEITO"));
        model.addAttribute("totalVereador", contagemPorCargo.get("VEREADOR"));
        model.addAttribute("totalVicePrefeitos", contagemPorCargo.get("VICE-PREFEITO"));
        model.addAttribute("totalPresidentes", contagemPorCargo.get("PRESIDENTE"));
        model.addAttribute("totalVicePresidentes", contagemPorCargo.get("VICE-PRESIDENTE"));

                        
        model.addAttribute("ano", ano);
        model.addAttribute("partido", partido);
        model.addAttribute("contagemPorCargo", contagemPorCargo);
        model.addAttribute("cargos", cargos); // Adiciona a lista de cargos
        model.addAttribute("contagemCandidatos", contagemCandidatos); // Adiciona a lista de contagens
        model.addAttribute("totalFiliados", totalFiliados);
        return "index"; // Nome do arquivo HTML sem extensão
    }

}