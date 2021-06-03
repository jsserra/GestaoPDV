/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.bean;

import br.pdv.dao.CelularDAO;
import br.pdv.dao.UsuarioDAO;
import br.pdv.model.Celular;
import br.pdv.model.Usuario;
import br.pdv.model.enums.Marca;
import br.pdv.util.pdvException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author julianos
 */
@Named
@ViewScoped
public class CelularBean implements Serializable {

    private Celular celular = new Celular();
    private Usuario usuario = new Usuario();

    private Marca marca;

    private Integer usuarioId;

    @Inject
    private CelularDAO daoCelular;

    @Inject
    private UsuarioDAO daoUsuario;

    //GET e SET
    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    //ACTION LiSTAS
    public List<Celular> getListaCelular() throws pdvException {
        return daoCelular.listar();
    }

    public List<Celular> getListaCelularMatriz() throws pdvException {
        return daoCelular.listar().stream().filter(x -> x.getFilial() == 0 && x.getAtivo() == true).collect(Collectors.toList());
    }

    public List<Celular> getListaCelularFilial() throws pdvException {
        return daoCelular.listar().stream().filter(x -> x.getFilial() == 1 && x.getAtivo() == true).collect(Collectors.toList());
    }

    public List<Usuario> getListaUsuarios() throws pdvException {
        List<Usuario> usuarios = daoUsuario.listar();
        Collections.sort(usuarios, Comparator.comparing(Usuario::getNome));
        return usuarios;
    }

    public Integer getTotalCelularMatriz() throws pdvException {
        if (getListaCelularMatriz().isEmpty()) {
            return 0;
        } else {
            return getListaCelularMatriz().size();
        }
    }

    public Integer getTotalCelularFilial() throws pdvException {
        if (getListaCelularFilial().isEmpty()) {
            return 0;
        } else {
            return getListaCelularFilial().size();
        }
    }

    //ACTiONS METHODS
    public String novoCelular() {
        this.celular = new Celular();
        return "formCelular?faces-redirect=true";
    }

    public String excluir(Celular celular) throws pdvException {
        daoCelular.excluir(celular);
        return "listaCelular?faces-redirect=true";
    }

    public String editar(Celular celular) throws pdvException {

        this.celular = celular;
        Boolean on = true;
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

        flash.put("cel", celular);
        flash.put("marca", celular.getMarca());
        flash.put("user", celular.getUsuario().getId());
        flash.put("on", on);

        return "formCelular?faces-redirect=true";
    }

    public void carregar() throws pdvException {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        Boolean on = (Boolean) flash.get("on");
        if (on != null && on == true) {
            this.celular = (Celular) flash.get("cel");
            this.marca = (Marca) flash.get("marca");
            this.usuarioId = (Integer) flash.get("user");

            this.usuario = daoUsuario.buscarId(usuarioId);
            celular.setUsuario(usuario);
            celular.setMarca(marca);
        }

    }

    public String salvar() throws pdvException {

        if (usuarioId == null) {
            this.usuario = daoUsuario.buscarId(1);
        } else {
            this.usuario = daoUsuario.buscarId(usuarioId);
        }

        if (marca == null) {
            this.marca = Marca.Motorola;
        }
        celular.setUsuario(usuario);
        celular.setMarca(marca);

        if (this.celular.getId() == null) {
            daoCelular.salvar(this.celular);
        } else {
            daoCelular.atualizar(celular);
        }

        this.celular = new Celular();

        return "listaCelular?faces-redirect=true";
    }

    public String desativar(Celular celular) throws pdvException {
        daoCelular.desativar(celular);
        return "listaCelular?faces-redirect=true";
    }


    /*Conversões e Mask
    public String format(String value) {

        if (value.equals("")) {
            return "VAZiO!!!";
        } else {
            String mac1 = value.substring(0, 2);
            String mac2 = value.substring(2, 4);
            String mac3 = value.substring(4, 6);
            String mac4 = value.substring(6, 8);
            String mac5 = value.substring(8, 10);
            String mac6 = value.substring(10, 12);
            String mac = mac1 + ":" + mac2 + ":" + mac3 + ":" + mac4 + ":" + mac5 + ":" + mac6;

            return mac;
        }
    }*/
}
