/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.VrstaManifestacije;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Petra
 */
@Stateless
public class VrstaManifestacijeFacade extends AbstractFacade<VrstaManifestacije> {

    @PersistenceContext(unitName = "CS230-ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VrstaManifestacijeFacade() {
        super(VrstaManifestacije.class);
    }
    
}
