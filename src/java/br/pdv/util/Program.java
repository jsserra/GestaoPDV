/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.util;


import br.pdv.model.Celular;
import br.pdv.model.Computer;
import br.pdv.model.Logg;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
//import javax.persistence.Query;

/**
 *
 * @author julianos
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws pdvException {
        // TODO code application logic here

        EntityManager em = new JPAUtil().getEntityManager();

        

        try {
            
            em.getTransaction().begin();
           /* Set<Logg> logs = new HashSet<>();
            List<Logg> logs = new ArrayList<>();
            

            
            Computer pc = em.find(Computer.class, 20);
            logs = new ArrayList<>(pc.getLogs());
            System.out.println("Inicamos a geração das Tabelas! \n pc = " + pc.getNome());
            Logg log1 = new Logg("Tipo teste1", "Log Teste1", LocalDateTime.now());
            System.out.println(log1);
            Logg log2 = new Logg("Tipo teste2", "Log Teste2", LocalDateTime.now());
            System.out.println(log2);
            Logg log3 = new Logg("Tipo teste3", "Log Teste3", LocalDateTime.now());
            System.out.println(log3);
            logs.add(log1);
            logs.add(log2);
            logs.add(log3);

            //Pdv pdv = em.find(Pdv.class, 1);
           System.out.println("__ Logs Antes do HashSet ___");
            for (Logg log : logs) {                
                System.out.println(log);
            }
            pc.setLogs(logs);
            em.persist(pc);
            em.getTransaction().commit();

            System.out.println("__ Logs Opós do HashSet ___");
            for (Logg log : logs) {                
                System.out.println(log);
            }

            //em.merge(pc);
            /* Pdv pdv = new Pdv("PDV01", 0);
            em.persist(pdv);
            Pdv pdv = em.find(Pdv.class, 56);
            Computer pc = em.find(Computer.class, 119);
            Computer pc = daoPC.buscarId(119);
            logs = new ArrayList<>(pc.getLogs());
            Log log1 = daoLog.buscarId(292);
            Log log2 = daoLog.buscarId(293);
            Log log3 = daoLog.buscarId(294);
            Log log4 = new Log("Tipo teste1", "Log Teste Erro novo4", LocalDateTime.now(),pc);
            Log log5 = new Log("Tipo teste1", "Log Teste Erro novo5", LocalDateTime.now(),pc);
            Log log6 = new Log("Tipo teste1", "Log Teste Erro novo6", LocalDateTime.now(),pc);
            log1.setDescricao("Log Teste Erro 292.2");
            log2.setDescricao("Log Teste Erro 293.2");
            log3.setDescricao("Log Teste Erro 294.2");

            logs.add(log1);
            logs.add(log2);
            logs.add(log3);
            logs.add(log4);
            logs.add(log5);
            logs.add(log6);

            Computer pc = new Computer("teste99", pdv);

            pc.setLogs(new HashSet<>(logs));
            System.out.println(pc);

            daoPC.atualizar(pc);
            List<Pdv> lista = em.createQuery("from Pdv").getResultList();
            for(Pdv p : lista){
                System.out.println(p);
            }*/

            //Set<Logg> listaLog = pc.getLogs();
            //listaPos = em.createQuery("from Pos").getResultList();
            //listaPos = new PosDAO().listar();*/
            
            Celular cel = em.find(Celular.class, 6);
            System.out.println(cel.getCod());
            cel.setDataUsuario(LocalDate.now());
            System.out.println(cel.getDataUsuario());
            
            em.merge(cel);
            em.getTransaction().commit();
            
            
            

            
            em.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar " + e.getMessage());

        }

    }

}
