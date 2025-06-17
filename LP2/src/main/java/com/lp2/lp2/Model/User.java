package com.lp2.lp2.Model;
import java.sql.Timestamp;


public class User {
    private int id;               // Identificador único do utilizador
    private String username;      // Nome de utilizador (ex.: João123)
    private String passwordHash;  // Senha do utilizador, armazenada como hash para segurança
    private String role;          // Papel do utilizador no sistema (ex.: Admin, Atleta, etc.)
    private boolean inative;      // Estado do utilizador (ativo ou inativo)
    private Integer athleteId;    // ID do atleta relacionado (pode ser nulo se o utilizador não for um atleta)
    private boolean incripted;
    private Boolean approved;
    private Timestamp ultimoLogin;

    /**
     * Obtém o ID único do utilizador.
     *
     * @return O identificador do utilizador.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID único do utilizador.
     *
     * @param id O identificador do utilizador a definir.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome de utilizador.
     *
     * @return O nome de utilizador.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Define o nome de utilizador.
     *
     * @param username O nome de utilizador a definir.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtém a senha do utilizador (armazenada como hash).
     *
     * @return O hash da senha do utilizador.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Define a senha do utilizador (como hash).
     *
     * @param passwordHash O hash da senha a definir.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Obtém o papel do utilizador no sistema.
     *
     * @return O papel do utilizador (ex.: Admin, Atleta).
     */
    public String getRole() {
        return role;
    }

    /**
     * Define o papel do utilizador no sistema.
     *
     * @param role O papel do utilizador a definir (ex.: Admin, Atleta).
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Obtém o estado de ativação ou inatividade do utilizador.
     *
     * @return Verdadeiro se o utilizador estiver inativo, falso caso contrário.
     */
    public boolean isInative() {
        return inative;
    }

    /**
     * Define o estado de ativação ou inatividade do utilizador.
     *
     * @param inative O estado de inatividade (verdadeiro para inativo, falso para ativo).
     */
    public void setInative(boolean inative) {
        this.inative = inative;
    }

    /**
     * Obtém o ID do atleta relacionado com o utilizador.
     * Este valor pode ser nulo caso o utilizador não seja um atleta.
     *
     * @return O ID do atleta ou nulo se o utilizador não for um atleta.
     */
    public Integer getAthleteId() {
        return athleteId;
    }

    /**
     * Define o ID do atleta relacionado com o utilizador.
     *
     * @param athleteId O ID do atleta a definir, ou nulo se o utilizador não for um atleta.
     */
    public void setAthleteId(Integer athleteId) {
        this.athleteId = athleteId;
    }

    public boolean isIncripted() {
        return incripted;
    }

    public void setIncripted(boolean incripted) {
        this.incripted = incripted;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
    public boolean isApproved() {
        return approved;
    }

    public Timestamp getUltimoLogin() { return ultimoLogin; }

    public void setUltimoLogin(Timestamp ultimoLogin) { this.ultimoLogin = ultimoLogin; }

}


