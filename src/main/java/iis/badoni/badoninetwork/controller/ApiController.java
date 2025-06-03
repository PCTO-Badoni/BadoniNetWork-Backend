package iis.badoni.badoninetwork.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import iis.badoni.badoninetwork.model.AltraSede;
import iis.badoni.badoninetwork.model.Annuncio;
import iis.badoni.badoninetwork.model.Area;
import iis.badoni.badoninetwork.model.Articolazione;
import iis.badoni.badoninetwork.model.Azienda;
import iis.badoni.badoninetwork.model.Booleano;
import iis.badoni.badoninetwork.model.Competenza;
import iis.badoni.badoninetwork.model.CompetenzaStudente;
import iis.badoni.badoninetwork.model.Contatto;
import iis.badoni.badoninetwork.model.Disponibile;
import iis.badoni.badoninetwork.model.Lingua;
import iis.badoni.badoninetwork.model.LinguaStudente;
import iis.badoni.badoninetwork.model.LivelloCompetenza;
import iis.badoni.badoninetwork.model.ModalitaContratto;
import iis.badoni.badoninetwork.model.Pronome;
import iis.badoni.badoninetwork.model.TipoAzienda;
import iis.badoni.badoninetwork.model.TipoContatto;
import iis.badoni.badoninetwork.model.TipoContratto;
import iis.badoni.badoninetwork.model.Utente;
import iis.badoni.badoninetwork.model.VerificaEmailStudente;
import iis.badoni.badoninetwork.repository.AltraSedeRepository;
import iis.badoni.badoninetwork.repository.AnnuncioRepository;
import iis.badoni.badoninetwork.repository.AreaRepository;
import iis.badoni.badoninetwork.repository.ArticolazioneRepository;
import iis.badoni.badoninetwork.repository.AziendaRepository;
import iis.badoni.badoninetwork.repository.CompetenzaRepository;
import iis.badoni.badoninetwork.repository.CompetenzaStudenteRepository;
import iis.badoni.badoninetwork.repository.ContattoRepository;
import iis.badoni.badoninetwork.repository.LinguaRepository;
import iis.badoni.badoninetwork.repository.LinguaStudenteRepository;
import iis.badoni.badoninetwork.repository.LivelloCompentezaRepository;
import iis.badoni.badoninetwork.repository.UtenteRepository;
import iis.badoni.badoninetwork.repository.VerificaEmailStudenteRepository;
import iis.badoni.badoninetwork.service.EmailService;
import iis.badoni.badoninetwork.service.Methods;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/api")
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
    private LivelloCompentezaRepository livelloCompentezeRepository;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private AltraSedeRepository altreSediRepository;
    @Autowired
    private CompetenzaStudenteRepository competenzeStudentiRepository;
    @Autowired
    private ContattoRepository contattiRepository;
    @Autowired
    private LinguaStudenteRepository lingueStudentiRepository;
    @Autowired
    private VerificaEmailStudenteRepository verificaEmailStudentiRepository;
    @Autowired
    private AnnuncioRepository annuncioRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private Methods methods;


    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> payload) {
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

    @PostMapping("/send-student-otp")
    public ResponseEntity<String> sendVerifyOTP(@RequestBody Map<String, String> payload) throws MessagingException, IOException {
        String email = payload.get("email");
        
        String codice = methods.generateCode();

        if (codice.equals("error")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nella generazione del codice\"}");
        }

        VerificaEmailStudente obj = new VerificaEmailStudente();
        obj.setEmail(email);
        obj.setCodice(codice);
        obj.setVerificato(Booleano.N);

        verificaEmailStudentiRepository.save(obj);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("codice", codice);
        emailService.sendHtmlMessage(email, "Verifica email", templateModel, "verify-response-template");


        return ResponseEntity.ok().body("{\"message\": \"Verifica inviata\"}");
    }

    @PostMapping("/verify-student-otp")
    public ResponseEntity<?> validateOTP(HttpSession httpSession, @RequestBody Map<String, String> requestBody) {
        int tries=-1;

        if (httpSession.getAttribute("tries") == null) {
            httpSession.setAttribute("tries", 1);
        } else {
            tries = (int) httpSession.getAttribute("tries")+1;
            httpSession.setAttribute("tries", tries);
        }

        if (tries>=5) {
            httpSession.removeAttribute("tries");
            //! Implementare un meccanismo
            return ResponseEntity.badRequest().body("{\"message\": \"Tentativi esauriti\"}");
        }

        VerificaEmailStudente verifica;

        verifica = verificaEmailStudentiRepository.findByEmail(requestBody.get("email")).orElse(null);

        if (verifica == null) {
            return ResponseEntity.badRequest().body("{\"message\": \"Email invalida\"}");
        }

        if (!verifica.getCodice().equals(requestBody.get("codice"))) {
            return ResponseEntity.badRequest().body("{\"message\": \"Codice invalido\"}");
        }

        verifica.setVerificato(Booleano.Y);
        verificaEmailStudentiRepository.save(verifica);
        return ResponseEntity.ok().body("{\"message\": \"Codice valido\"}");
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
    public ResponseEntity<List<LivelloCompetenza>> getAllLivelliCompetenze() {
        List<LivelloCompetenza> livelli_competenza = livelloCompentezeRepository.findAll();
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
    public ResponseEntity<List<Azienda>> getAllAziendaWaiting() {
        List<Azienda> aziendaWaiting = aziendaRepository.findByType(TipoAzienda.W);
        return ResponseEntity.ok(aziendaWaiting);
    }

    @GetMapping("/get-all-aziende_approved")
    public ResponseEntity<List<Azienda>> getAllAziendaApproved() {
        List<Azienda> aziendaApproved = aziendaRepository.findByType(TipoAzienda.A);
        return ResponseEntity.ok(aziendaApproved);
    }

    @GetMapping("/get-all-aziende_registered")
    public ResponseEntity<List<Azienda>> getAllAziendaRegistered() {
        List<Azienda> azienda = aziendaRepository.findByType(TipoAzienda.R);
        return ResponseEntity.ok(azienda);
    }

    @GetMapping("/get-all-aziende")
    public ResponseEntity<List<Azienda>> getAllAzienda() {
        List<Azienda> azienda = aziendaRepository.findAll();
        return ResponseEntity.ok(azienda);
    }

    @GetMapping("/get-all-altre_sedi")
    public ResponseEntity<List<AltraSede>> getAllAltreSedi() {
        List<AltraSede> altre_sedi = altreSediRepository.findAll();
        return ResponseEntity.ok(altre_sedi);
    }
    
    @GetMapping("/get-all-competenze_studenti")
    public ResponseEntity<List<CompetenzaStudente>> getAllCompetenzeStudenti() {
        List<CompetenzaStudente> competenze_studenti = competenzeStudentiRepository.findAll();
        return ResponseEntity.ok(competenze_studenti);
    }

    @GetMapping("/get-all-contatti")
    public ResponseEntity<List<Contatto>> getAllContatti() {
        List<Contatto> contatti = contattiRepository.findAll();
        return ResponseEntity.ok(contatti);
    }

    @GetMapping("/get-all-lingue_studenti")
    public ResponseEntity<List<LinguaStudente>> getAllLingueStudenti() {
        List<LinguaStudente> lingue_studenti = lingueStudentiRepository.findAll();
        return ResponseEntity.ok(lingue_studenti);
    }

    @GetMapping("/get-annuncio")
    public ResponseEntity<?> getAnnuncioByEmail(@RequestParam String email) {
        List<Annuncio> annunci = annuncioRepository.findByAzienda_email(email);
        return ResponseEntity.ok(annunci);
    }

    @GetMapping("/get-all-annunci")
    public ResponseEntity<?> getAllAnnunci() {
        List<Annuncio> annunci = annuncioRepository.findAll();
        return ResponseEntity.ok(annunci);
    }

    @DeleteMapping("/remove-annuncio")
    public ResponseEntity<?> removeAnnuncio(@RequestParam String id) {
        Optional<Annuncio> annuncio = annuncioRepository.findById(Integer.parseInt(id));

        if (!annuncio.isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Id inesistente\"}");
        }

        annuncioRepository.delete(annuncio.get());

        return ResponseEntity.ok().body("{\"message\": \"Annuncio rimosso\"}");
    }

    @PostMapping("/set-user-competences")
    public ResponseEntity<String> setUserCompetences(@RequestBody List<Map<String, String>> payload) {
        try {
            for (Map<String, String> competence : payload) {
                int idCompetenza = Integer.parseInt(competence.get("idcompetenza"));
                String email = competence.get("email");
                String idlivello = competence.get("idlivello");

                Optional<Utente> studente = utenteRepository.findByEmail(email);
                Optional<Competenza> competenza = competenzaRepository.findById(idCompetenza);
                Optional<LivelloCompetenza> livello = livelloCompentezeRepository.findById(idlivello);

                if (!studente.isPresent() || !competenza.isPresent() || !livello.isPresent()) {
                    return ResponseEntity.ok().body("{\"message\": \"Dati errati\"}");
                }

                CompetenzaStudente competenzeStudenti = new CompetenzaStudente();
                competenzeStudenti.setStudente(studente.get());
                competenzeStudenti.setCompetenza(competenza.get());
                competenzeStudenti.setLivelloCompetenza(livello.get());

                competenzeStudentiRepository.save(competenzeStudenti);
            }

            return ResponseEntity.ok().body("{\"message\": \"Competenze salvate\"}");
        } catch (Exception e) {
            return ResponseEntity.ok().body("{\"message\": \"Errore nel salvataggio\"}");
        }
    }

    @PostMapping("/set-user-languages")
    public ResponseEntity<String> setUserLanguages(@RequestBody List<Map<String, String>> payload) {
        try {
            for (Map<String, String> language : payload) {
                int idlingua = Integer.parseInt(language.get("idlingua"));
                String email = language.get("email");
                String idlivello = language.get("idlivello");

                Optional<Lingua> lingua = linguaRepository.findById(idlingua);
                Optional<LivelloCompetenza> livello = livelloCompentezeRepository.findById(idlivello);
                Optional<Utente> studente = utenteRepository.findByEmail(email);

                if (!studente.isPresent() || !lingua.isPresent() || !livello.isPresent()) {
                    return ResponseEntity.ok().body("{\"message\": \"Dati errati\"}");
                }

                LinguaStudente lingue_studenti = new LinguaStudente();
                lingue_studenti.setLingua(lingua.get());
                lingue_studenti.setStudente(studente.get());
                lingue_studenti.setLivello(livello.get());
                lingueStudentiRepository.save(lingue_studenti);
            }

            return ResponseEntity.ok().body("{\"message\": \"Lingue salvate\"}");
        } catch (Exception e) {
            return ResponseEntity.ok().body("{\"message\": \"Errore nel salvataggio\"}");
        }
    }

    @PostMapping("/set-other-places")
    public ResponseEntity<String> setOtherPlaces(@RequestBody List<Map<String, String>> payload) {
        try {
            for (Map<String, String> altresedi : payload) {
                String indirizzo = altresedi.get("indirizzo");
                String email = altresedi.get("email");
                String cap = altresedi.get("cap");
                String citta = altresedi.get("citta");

                Optional<Azienda> azienda = aziendaRepository.findByEmail(email);

                if (!azienda.isPresent()) {
                    return ResponseEntity.ok().body("{\"message\": \"Email errata\"}");
                }

                AltraSede altre_sedi = new AltraSede();
                altre_sedi.setAzienda(azienda.get());
                altre_sedi.setIndirizzo(indirizzo);
                altre_sedi.setCap(cap);
                altre_sedi.setCitta(citta);
                altreSediRepository.save(altre_sedi);
            }

            return ResponseEntity.ok().body("{\"message\": \"Sedi salvate\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nel salvataggio\"}");
        }
    }

    @PostMapping("/get-user-competences")
    public ResponseEntity<?> getUserCompetences(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            List<CompetenzaStudente> competenze = competenzeStudentiRepository.findByStudente_email(email);
            return ResponseEntity.ok().body(competenze);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nel caricamento\"}");
        }
    }

    @PostMapping("/get-user-languages")
    public ResponseEntity<?> getUserLanguages(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            List<LinguaStudente> lingue = lingueStudentiRepository.findByStudente_email(email);
            return ResponseEntity.ok().body(lingue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nel caricamento\"}");
        }
    }

    @PostMapping("/get-other-places")
    public ResponseEntity<?> getOtherPlaces(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            List<AltraSede> sedi = altreSediRepository.findByAzienda_email(email);
            return ResponseEntity.ok().body(sedi);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nel caricamento\"}");
        }
    }

    @PostMapping("/add-contatto")
    public ResponseEntity<String> addContatto(@RequestBody Map<String, String> payload) {
        try {
                String emailazienda = payload.get("emailazienda");
                String emailstudente = payload.get("emailstudente");
                String dataoraStr  = payload.get("dataora");
                String visualizzatoStr = payload.get("visualizzato");
                String tipoStr = payload.get("tipo");
                String messaggio = payload.get("messaggio");

                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                LocalDateTime dataora = LocalDateTime.parse(dataoraStr, formatter);

                Booleano visualizzatoEnum = Booleano.valueOf(visualizzatoStr);
                TipoContatto tipoEnum = TipoContatto.valueOf(tipoStr);

                Optional<Utente> studente = utenteRepository.findByEmail(emailstudente);
                Optional<Azienda> azienda = aziendaRepository.findByEmail(emailazienda);

                if (!studente.isPresent() || !azienda.isPresent()) {
                    return ResponseEntity.badRequest().body("{\"message\": \"Email errate\"}");
                }
                
                Contatto contatto = new Contatto();
                contatto.setStudente(studente.get());
                contatto.setAzienda(azienda.get());
                contatto.setMessaggio(messaggio);
                contatto.setDataora(dataora);
                contatto.setTipo(tipoEnum);
                contatto.setVisualizzato(visualizzatoEnum);

                contattiRepository.save(contatto);  
            return ResponseEntity.ok().body("{\"message\": \"Contatto salvato\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nel salvataggio\"}");
        }
    }

    @PostMapping("/add-annuncio")
    public ResponseEntity<String> addAnnuncio(@RequestBody Map<String, String> payload) {
        try {
                String ruolo = payload.get("ruolo");
                TipoContratto contratto = TipoContratto.valueOf(payload.get("contratto"));
                ModalitaContratto modalita = ModalitaContratto.valueOf(payload.get("modalita"));
                float retribuzione = Float.parseFloat(payload.get("retribuzione"));
                String descrizione = payload.get("descrizione");
                String emailazienda = payload.get("email_azienda");

                Optional<Azienda> azienda = aziendaRepository.findByEmail(emailazienda);

                if (!azienda.isPresent()) {
                    return ResponseEntity.badRequest().body("{\"message\": \"Azienda inesistente\"}");
                }
                
                Annuncio annuncio = new Annuncio();
                annuncio.setRuolo(ruolo);
                annuncio.setContratto(contratto);
                annuncio.setModalita(modalita);
                annuncio.setRetribuzione(retribuzione);
                annuncio.setDescrizione(descrizione);
                annuncio.setAzienda(azienda.get());

                annuncioRepository.save(annuncio);  
            return ResponseEntity.ok().body("{\"message\": \"Annuncio salvato\"}");
        } catch (Exception e) {
            String message = "{\"message\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}";
            return ResponseEntity.badRequest().body(message);
        }
    }

    @PostMapping("/add-competenza")
    public ResponseEntity<String> addCompetenza(@RequestBody Map<String, String> payload) {
        try {
                String descrizione = payload.get("descrizione");

                Competenza competenza = new Competenza();
                competenza.setDescrizione(descrizione);

                competenzaRepository.save(competenza);
            return ResponseEntity.ok().body("{\"message\": \"competenza aggiunta\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nel salvataggio\"}");
        }
    }

    @PostMapping("/add-lingua")
    public ResponseEntity<String> addLingua(@RequestBody Map<String, String> payload) {
        try {
                String descrizione = payload.get("descrizione");

                Lingua lingua = new Lingua();
                lingua.setDescrizione(descrizione);

                linguaRepository.save(lingua);
            return ResponseEntity.ok().body("{\"message\": \"lingua aggiunta\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nel salvataggio\"}");
        }
    }

    @PostMapping("/add-livello-competenze")
    public ResponseEntity<String> addLivelloCompetenze(@RequestBody Map<String, String> payload) {
        try {
                String descrizione = payload.get("descrizione");

                LivelloCompetenza livello = new LivelloCompetenza();
                livello.setDescrizione(descrizione);

                livelloCompentezeRepository.save(livello);
            return ResponseEntity.ok().body("{\"message\": \"lingua aggiunta\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nel salvataggio\"}");
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateAccount(@RequestBody Map<String, String> updates, HttpSession session) {
        Object account = session.getAttribute("user-account");

        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": \"Utente non autenticato\"}");
        }

        // Aggiornamento per Azienda
        if (account instanceof Azienda azienda) {

            updates.forEach((key, value) -> {
                switch (key) {
                    case "ragionesociale" -> azienda.setRagionesociale(value);
                    case "telefono" -> azienda.setTelefono(value);
                    case "indirizzo" -> azienda.setIndirizzo(value);
                    case "cognomereferente" -> azienda.setCognomereferente(value);
                    case "nomereferente" -> azienda.setNomereferente(value);
                    case "telreferente" -> azienda.setTelreferente(value);
                    case "emailreferente" -> azienda.setEmailreferente(value);
                }
            });

            aziendaRepository.save(azienda);
            session.setAttribute("user-account", azienda);
            return ResponseEntity.ok(azienda);
        }

        // Aggiornamento per Studente
        if (account instanceof Utente studente) {

            updates.forEach((key, value) -> {
                switch (key) {
                    case "emailpersonale" -> studente.setEmailpersonale(value);
                    case "nome" -> studente.setNome(value);
                    case "cognome" -> studente.setCognome(value);
                    case "pronomi" -> studente.setPronomi(Pronome.valueOf(value));
                    case "telefono" -> studente.setTelefono(value);
                    case "indirizzo" -> studente.setIndirizzo(value);
                    case "datanascita" -> studente.setDatanascita(LocalDate.parse(value));
                    case "disponibile" -> studente.setDisponibile(Disponibile.valueOf(value));
                    case "curriculum" -> studente.setCurriculum(value);
                    case "note" -> studente.setNote(value);
                }
            });

            utenteRepository.save(studente);
            session.setAttribute("user-account", studente);
            return ResponseEntity.ok(studente);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Tipo utente non riconosciuto\"}");
    }

    
}