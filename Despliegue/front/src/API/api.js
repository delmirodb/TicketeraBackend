import axios from "axios";

export const url_api = 'http://localhost:5000'

export const api = {
    register: function(nombre, apellido, email, password){
        return axios.post(`${url_api}/register?nombre=${nombre}&apellido=${apellido}&email=${email}&password=${password}`);
    },
    login: function(email, password){
        return axios.post(`${url_api}/login?email=${email}&password=${password}`);
    },
    isLogged: function(token){
        return axios.post(`${url_api}/loggedIn?token=${token}`);
    },
    homeEventos: function(){
        return axios.get(`${url_api}/home/eventos`);
    },
    getEvento: function(id){
        return axios.get(`${url_api}/evento?id=${id}`)
    },
    getAsientosDisponibles: function(tipo, id){
        return axios.get(`${url_api}/asientosDisponibles?tipo=${tipo}&id=${id}`)
    },
    comprarEntradas: function(data){
        return axios.post(`${url_api}/comprarEntradas${data}`)
    },
    getCompra: function(id, token){
        return axios.post(`${url_api}/compra?id=${id}&usuarioSessionID=${token}`)
    },
    getComprasUsuario: function(token){
        return axios.get(`${url_api}/usuario/compras?usuarioSessionID=${token}`);
    },
    validarAcceso: function(id, token){
        return axios.post(`${url_api}/stream?id=${id}&usuarioSessionID=${token}`)
    },
    validarTicket: function(id, token){
        return axios.post(`${url_api}/validar?id=${id}&usuarioSessionID=${token}`)
    }
}