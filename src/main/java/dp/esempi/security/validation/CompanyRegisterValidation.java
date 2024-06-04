package dp.esempi.security.validation;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AziendaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
@SuppressWarnings("unused")
@Configuration
@Component
public class CompanyRegisterValidation implements ConstraintValidator<AziendaValida, Azienda>{

    private static final CompanyRegisterValidation holder= new CompanyRegisterValidation();
    private AziendaRepository aziendaRepository;

    @Bean(name = "company_validator")
    public static CompanyRegisterValidation bean(AziendaRepository aziendaRepository) {
        holder.aziendaRepository=aziendaRepository;
        return holder;
    }

    @Override
    public void initialize(AziendaValida constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Azienda a, ConstraintValidatorContext constraintValidatorContext) {
        boolean valido=true;
        Optional<Azienda> aziendaFind=holder.aziendaRepository.findByEmail(a.getEmail());
        if(!aziendaFind.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email già esistente")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            valido=false;
        }

        if (valido == false) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Richiesta già inviata")
                    .addPropertyNode("errore")
                    .addConstraintViolation();
        }

        boolean valido2 = checkEmailAzienda(constraintValidatorContext, a.getEmail());
        
        boolean valido3 = checkEmailApproved(constraintValidatorContext, a.getEmail());

        if (valido && valido2 && valido3) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }

    private boolean checkEmailAzienda(ConstraintValidatorContext constraintValidatorContext, String email) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/network";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String emailToCheck = email;

            // Prepare the SQL query
            String sql = "SELECT COUNT(*) FROM azienda WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, emailToCheck);

                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count > 0) {
                            constraintValidatorContext.disableDefaultConstraintViolation();
                            constraintValidatorContext.buildConstraintViolationWithTemplate("Account esistente")
                                    .addPropertyNode("errore")
                                    .addConstraintViolation();
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkEmailApproved(ConstraintValidatorContext constraintValidatorContext, String email) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/network";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String emailToCheck = email;

            // Prepare the SQL query
            String sql = "SELECT COUNT(*) FROM aziende_approved WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, emailToCheck);

                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count > 0) {
                            constraintValidatorContext.disableDefaultConstraintViolation();
                            constraintValidatorContext.buildConstraintViolationWithTemplate("Account già approvato")
                                    .addPropertyNode("errore")
                                    .addConstraintViolation();
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
