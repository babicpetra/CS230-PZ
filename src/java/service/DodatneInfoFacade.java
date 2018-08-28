/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.DodatneInfo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Petra
 */
@Stateless
public class DodatneInfoFacade extends AbstractFacade<DodatneInfo> {

    @PersistenceContext(unitName = "CS230-ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DodatneInfoFacade() {
        super(DodatneInfo.class);
    }
    
}
