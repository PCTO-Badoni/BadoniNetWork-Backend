package dp.esempi.security.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dp.esempi.security.model.AltreSedi;
import dp.esempi.security.model.Area;
import dp.esempi.security.model.Articolazione;
import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.AziendaApproved;
import dp.esempi.security.model.AziendaWaiting;
import dp.esempi.security.model.Competenza;
import dp.esempi.security.model.CompetenzeStudenti;
import dp.esempi.security.model.Contatti;
import dp.esempi.security.model.Lingua;
import dp.esempi.security.model.LingueStudenti;
import dp.esempi.security.model.LivelloCompetenze;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AltreSediRepository;
import dp.esempi.security.repository.AreaRepository;
import dp.esempi.security.repository.ArticolazioneRepository;
import dp.esempi.security.repository.AziendaApprovedRepository;
import dp.esempi.security.repository.AziendaRepository;
import dp.esempi.security.repository.AziendaWaitingRepository;
import dp.esempi.security.repository.CompetenzaRepository;
import dp.esempi.security.repository.CompetenzeStudentiRepository;
import dp.esempi.security.repository.ContattiRepository;
import dp.esempi.security.repository.LinguaRepository;
import dp.esempi.security.repository.LingueStudentiRepository;
import dp.esempi.security.repository.LivelloCompentezeRepository;
import dp.esempi.security.repository.UtenteRepository;




@RequestMapping("/api")
@CrossOrigin (origins = {"http://localhost:3001", "http://127.0.0.1:3001"})
@RestController
public class ApiController {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private CompetenzaRepository competenzaRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private ArticolazioneRepository articolazioneRepository;
    @Autowired
    private LinguaRepository linguaRepository;
    @Autowired
    private LivelloCompentezeRepository livelloCompentezeRepository;
    @Autowired
    private AziendaWaitingRepository aziendaWaitingRepository;
    @Autowired
    private AziendaApprovedRepository aziendaApprovedRepository;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private AltreSediRepository altreSediRepository;
    @Autowired
    private CompetenzeStudentiRepository competenzeStudentiRepository;
    @Autowired
    private ContattiRepository contattiRepository;
    @Autowired
    private LingueStudentiRepository lingueStudentiRepository;


    @PostMapping("/verify-email")
    public ResponseEntity<String> postMethodName(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        
        if (!email.endsWith("iisbadoni.edu.it")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Email invalida\"}");
        }

        Optional<Utente> utenteFind= utenteRepository.findByEmail(email);
        if(!utenteFind.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Email già esistente\"}");
        }

        
        return ResponseEntity.ok().body("{\"message\": \"Email valida\"}");
    }

    @GetMapping("/get-all-competenze")
    public ResponseEntity<List<Competenza>> getAllCompetences() {
        List<Competenza> competenze = competenzaRepository.findAll();
        return ResponseEntity.ok(competenze);
    }

    @GetMapping("/get-all-aree")
    public ResponseEntity<List<Area>> getAllAree() {
        List<Area> aree = areaRepository.findAll();
        return ResponseEntity.ok(aree);
    }

    @GetMapping("/get-all-articolazioni")
    public ResponseEntity<List<Articolazione>> getAllArticolazioni() {
        List<Articolazione> articolazioni = articolazioneRepository.findAll();
        return ResponseEntity.ok(articolazioni);
    }

    @GetMapping("/get-all-lingue")
    public ResponseEntity<List<Lingua>> getAllLingue() {
        List<Lingua> lingua = linguaRepository.findAll();
        return ResponseEntity.ok(lingua);
    }

    @GetMapping("/get-all-livelli_competenze")
    public ResponseEntity<List<LivelloCompetenze>> getAllLivelliCompetenze() {
        List<LivelloCompetenze> livelli_competenza = livelloCompentezeRepository.findAll();
        return ResponseEntity.ok(livelli_competenza);
    }

    @GetMapping("/get-all-studenti")
    public ResponseEntity<List<Utente>> getAllStudenti() {
        List<Utente> utenti = utenteRepository.findAll();

        for (Utente utente : utenti) {
            utente.setPassword(null);
        }
        return ResponseEntity.ok(utenti);
    }

    @GetMapping("/get-all-aziende_waiting")
    public ResponseEntity<List<AziendaWaiting>> getAllAziendaWaiting() {
        List<AziendaWaiting> azienda_waiting = aziendaWaitingRepository.findAll();
        return ResponseEntity.ok(azienda_waiting);
    }

    @GetMapping("/get-all-aziende_approved")
    public ResponseEntity<List<AziendaApproved>> getAllAziendaApproved() {
        List<AziendaApproved> azienda_approved = aziendaApprovedRepository.findAll();
        return ResponseEntity.ok(azienda_approved);
    }

    @GetMapping("/get-all-aziende")
    public ResponseEntity<List<Azienda>> getAllAzienda() {
        List<Azienda> azienda = aziendaRepository.findAll();
        return ResponseEntity.ok(azienda);
    }

    @GetMapping("/get-all-altre_sedi")
    public ResponseEntity<List<AltreSedi>> getAllAltreSedi() {
        List<AltreSedi> altre_sedi = altreSediRepository.findAll();
        return ResponseEntity.ok(altre_sedi);
    }
    
    @GetMapping("/get-all-competenze_studenti")
    public ResponseEntity<List<CompetenzeStudenti>> getAllCompetenzeStudenti() {
        List<CompetenzeStudenti> competenze_studenti = competenzeStudentiRepository.findAll();
        return ResponseEntity.ok(competenze_studenti);
    }

    @GetMapping("/get-all-contatti")
    public ResponseEntity<List<Contatti>> getAllContatti() {
        List<Contatti> contatti = contattiRepository.findAll();
        return ResponseEntity.ok(contatti);
    }

    @GetMapping("/get-all-lingue_studenti")
    public ResponseEntity<List<LingueStudenti>> getAllLingueStudenti() {
        List<LingueStudenti> lingue_studenti = lingueStudentiRepository.findAll();
        return ResponseEntity.ok(lingue_studenti);
    }

    @PostMapping("/send-user-competences")
    public ResponseEntity<String> setUserCompetences(@RequestBody List<Map<String, String>> payload) {
        try {
            for (Map<String, String> competence : payload) {
                int idCompetenza = Integer.parseInt(competence.get("idcompetenza"));
                String email = competence.get("email");
                String idLivello = competence.get("idlivello");

                CompetenzeStudenti competenzeStudenti = new CompetenzeStudenti();
                competenzeStudenti.setEmail(email);
                competenzeStudenti.setIdcompetenza(idCompetenza);
                competenzeStudenti.setIdlivello(idLivello);

                competenzeStudentiRepository.save(competenzeStudenti);
            }

            return ResponseEntity.ok().body("{\"message\": \"Competenze salvate\"}");
        } catch (Exception e) {
            return ResponseEntity.ok().body("{\"message\": \"Errore nel salvataggio\"}");
        }
    }

    // @PostMapping("/send-user-languages")
    // public ResponseEntity<String> setUserLanguages(@RequestBody List<Map<String, String>> payload) {
    //     try {
    //         for (Map<String, String> language : payload) {
    //             int id = Integer.parseInt(language.get("idcompetenza"));
    //             String email = language.get("email");
    //             String livello = language.get("idlivello");

    //             LingueStudenti lingue_studenti = new LingueStudenti(1, email, id, livello);
    //             lingueStudentiRepository.save(lingue_studenti);
    //         }

    //         return ResponseEntity.ok().body("{\"message\": \"Lingue salvate\"}");
    //     } catch (Exception e) {
    //         return ResponseEntity.ok().body("{\"message\": \"Errore nel salvataggio\"}");
    //     }
    // }
    
}