package com.lp2.lp2.Session;

import com.lp2.lp2.Model.User;

/**
 * Classe que gerencia a sessão do utilizador.
 * Esta classe armazena e fornece acesso ao utilizador logado no sistema,
 * permitindo verificar se o utilizador está autenticado,
 * obter o ID do utilizador e verificar o seu papel.
 */
public class Session {
    private static User user;  // O utilizador logado na sessão

    /**
     * Define o utilizador logado na sessão.
     *
     * @param loggedUser O utilizador a ser definido como logado.
     */
    public static void setUser(User loggedUser) {
        user = loggedUser;
    }

    /**
     * Retorna o utilizador logado na sessão.
     *
     * @return O utilizador logado, ou null se não houver utilizador logado.
     */
    public static User getUser() {
        return user;
    }

    /**
     * Limpa os dados da sessão, removendo o utilizador logado.
     */
    public static void clear() {
        user = null;
    }

    /**
     * Verifica se existe um utilizador logado na sessão.
     *
     * @return Verdadeiro se houver um utilizador logado, falso caso contrário.
     */
    public static boolean isUserLoggedIn() {
        return user != null;
    }

    /**
     * Retorna o ID do utilizador logado.
     * Se não houver utilizador logado, retorna -1.
     *
     * @return O ID do utilizador logado, ou -1 se não houver utilizador logado.
     */
    public static int getLoggedUserId() {
        return user != null ? user.getId() : -1; // Retorna -1 se não houver utilizador logado
    }

    /**
     * Verifica se o utilizador logado tem um papel específico.
     *
     * @param role O papel a ser verificado (ex.: "Admin", "Atleta").
     * @return Verdadeiro se o utilizador logado tiver o papel especificado, falso caso contrário.
     */
    public static boolean hasRole(String role) {
        return user != null && user.getRole().equalsIgnoreCase(role);
    }
}
