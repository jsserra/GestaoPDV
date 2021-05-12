/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.dao;

import br.pdv.model.Usuario;
import br.pdv.util.pdvException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author julianos
 */
public class UsuarioDAO implements Serializable{
    
    @Inject
    EntityManager em;
    
    private GeneralDAO<Usuario> dao;
    
    @PostConstruct
    void start(){
        dao = new GeneralDAO<>(this.em, Usuario.class);
    }
    
    public List<Usuario> listar() throws pdvException{
        return dao.listar();
    }
    
    public Usuario buscarId(Integer id) throws pdvException{
        return dao.buscarId(id);
    }
    
}
