/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author julianos
 */
public class JPAUtil {
    
    private static EntityManagerFactory entityManagerfactory;
    
    static{
        try{
            entityManagerfactory = Persistence.createEntityManagerFactory("GestaoPDVPU");
        }catch(Exception e){
            System.out.println("Erro gerar seção JPA" + e);
        }
    }
    
    @Produces
    @RequestScoped
    public EntityManager getEntityManager(){
        return entityManagerfactory.createEntityManager();
    }
    
    public void close(@Disposes EntityManager entityManager){
        try{
            entityManager.close();
        }catch(Exception e){
            System.out.println("Erro fechar seção JPA" + e.getMessage());
        }
    }
}
