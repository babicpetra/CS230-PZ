/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs230.pz.session;

/**
 *
 * @author Petra
 */


import entities.Korisnik;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
    }

    public static Korisnik getKorisnik() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        return (Korisnik) session.getAttribute("data");
    }

    public static String getKorisnikId() {
        HttpSession session = getSession();
        if (session != null) {
            return (String) session.getAttribute("ID_KORISNIK");
        } else {
            return null;
        }
    }
}
