/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.authService;

import entities.Korisnik;
import service.authService.SessionUtils;
import java.io.Serializable;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Petra
 */
@ManagedBean
@SessionScoped
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "CS230-ProjectPU")
    private EntityManager em;

    private String username;
    private String password;
    private String rola;
    private boolean loggedIn = false;

    public boolean isLoggedIn() {
        return loggedIn;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }
    

    //validate login
    public String login() {
        Korisnik user = null;
        boolean valid;

        try {
            user = (Korisnik) em.createNamedQuery("Korisnik.findByUsername").setParameter("username", username).getSingleResult();
            valid = validateUser(user);
        } catch (NoResultException e) {
            valid = false;
        }

        if (valid) {
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("data", user);
            loggedIn = true;
            System.out.println(user.getRola());
            if (user.getRola().equalsIgnoreCase("admin")) {
            return "/adminPanel";
            }
            else{
               return "korisnikPanel";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Uneti podaci nisu ispravni.","Molimo Vas da proverite podatke i pokušate ponovo."));
            return "/korisnik/Login";
        }
        
    }

    private boolean validateUser(Korisnik user) {
        if (username.equalsIgnoreCase(user.getUsername()) && password.equals(user.getPassword())) {
            return true;
        }
        else
        return false;
    }



    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        return "/korisnik/Login";
    }


}
