import React, { useEffect, useState } from 'react';
import { Container, Spinner } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import { api, url_api } from '../API/api';

function Evento() {
    let data = useParams();
    let id = data.id;

    const [evento, setEvento] = useState(false);
    const [asientos, setAsientos] = useState(false);
    let tipoEvento = 0;

    const getEvento = async () => {
        await api.getEvento(id)
            .then(response => {
                setEvento(response.data);
                tipoEvento = response.data.tipo.id;
                getAsientos();
            }).catch(error => {
                window.location.href = "/";
                console.log(error)
            })
    }

    const getAsientos = async () => {
        let tipo = document.getElementById('tipoEntrada').value;
        await api.getAsientosDisponibles(tipo, id)
            .then(response => {
                let optionNEntradas = ''
                setAsientos(response.data);
                if (response.data) {
                    for (let i = 1; i <= response.data; i++) {
                        optionNEntradas += "<option value='" + i + "'>" + i + "</option>"
                        if(tipoEvento === 4){
                            if (i === 1) {
                                break;
                            }
                        } else {
                            if (i === 5) {
                                break;
                            }
                        }
                        
                    }
                    document.getElementById('numeroEntradas').innerHTML = optionNEntradas;
                    getEntradas();
                }
            })
    }

    const getEntradas = async () => {
        let tipo = document.getElementById('tipoEntrada').value;
        let datosEntrada = ''
        let nEntradas = document.getElementById('numeroEntradas').value
        datosEntrada += "<input id='Tipo' name='Tipo' value='" + tipo + "' style='display:none'></input>"
        datosEntrada += "<input id='nEntradas' name='nEntradas' value='" + nEntradas + "' style='display:none'></input>"
        for (let i = 1; i <= nEntradas; i++) {
            datosEntrada += "<div style='margin-bottom: 15px'>"
            datosEntrada += "<h6>Entrada Nº" + i + "</h6>"
            datosEntrada += "<input id='nombre" + i + "' name='nombre" + i + "' placeholder='Nombre'></input><br />"
            datosEntrada += "<input id='apellido" + i + "' name='apellido" + i + "' placeholder='Apellido'></input><br />"
            datosEntrada += "<input id='dni" + i + "' name='dni" + i + "' placeholder='DNI'></input>"
            datosEntrada += "</div>"
        }
        document.getElementById('datosEntrada').innerHTML = datosEntrada;
    }

    useEffect(() => {
        async function showEvento() {
            getEvento();
        }
        showEvento()
    }, [])

    const getCookieValue = (name) => (
        document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)')?.pop() || ''
    )

    const sendDatos = async () => {
        let data = '?'
        let form = document.forms.datosEntrada;
        let formData = new FormData(form);
        formData.append("usuarioSessionID", getCookieValue('sess'))
        let totalEntradas = parseInt(formData.get('nEntradas'));
        for (let i = 1; i <= totalEntradas; i++) {
            data += 'nombre' + i + '=' + formData.get('nombre' + i)
            data += '&'
            data += 'apellido' + i + '=' + formData.get('apellido' + i)
            data += '&'
            data += 'dni' + i + '=' + formData.get('dni' + i)
            if (i !== totalEntradas) {
                data += '&'
            }
        }
        data += '&Evento=' + evento.nombre
        data += '&Tipo=' + formData.get('Tipo')
        data += '&nEntradas=' + formData.get('nEntradas')
        data += '&usuarioSessionID=' + formData.get('usuarioSessionID')
        document.getElementById('formEntradas').classList.add('hidden');
        document.getElementById('spinner').classList.add('show');
        await api.comprarEntradas(data)
            .then(response => {
                window.location.replace("/compra/" + response.data)
            })
    }

    if (evento) {
        return (
            <>
                <Container className='tarjetaEvento'>
                    <div className='datosEvento'>
                        <h5>{evento.fecha}</h5>
                        <h2><b>{evento.nombre}</b></h2>
                        <h4><b>{evento.ciudad}</b></h4>
                        <h6>{evento.descripcion}</h6>
                    </div>
                    <img src={url_api + '/img/evento_' + evento.id + '.jpg'} />
                </Container>
                <Container>
                    <div id='spinner' style={{ display: "none", marginTop: 40, marginBottom: 40 }}>
                        <Spinner animation="border" style={{marginBottom: 10}} />
                        <h3>Procesando tu compra...</h3>
                    </div>
                    <div id='formEntradas' className='formEntradas'>
                        {getCookieValue('sess') !== '' && evento.tipo.id !== 4
                        ?
                        <h6 style={{paddingTop: 20}}>Tipo de entrada
                            <select id='tipoEntrada' defaultValue={1} onChange={getAsientos}>
                                <option value='1'>General - 10.00€</option>
                                <option value='2'>VIP - 30.00€</option>
                            </select>
                        </h6>
                        : getCookieValue('sess') !== '' && evento.tipo.id === 4 ?
                        <h6 style={{paddingTop: 20}}>Tipo de entrada
                            <select id='tipoEntrada' defaultValue={1} onChange={getAsientos}>
                                <option value='1'>General - 10.00€</option>
                            </select>
                        </h6>
                        :
                        <div style={{ paddingBottom: 40, paddingTop: 40}}>
                            <p>Por favor, inicia sesión o regístrate para realizar tu compra.</p>
                        </div>
                        }
                        {asientos && getCookieValue('sess') !== ''
                            ?
                            <>
                                <h6>Entradas
                                    <select id='numeroEntradas' defaultValue={1} onChange={getEntradas}>

                                    </select>
                                </h6>
                                <div className='datosEntrada'  style={{ paddingBottom: 40}}>
                                    <form id='datosEntrada'>
                                        <h6>Entrada Nº1</h6>
                                        <div>
                                            <input id='nombre1' name='nombre1' placeholder='Nombre'></input><br />
                                            <input id='apellido1' name='apellido1' placeholder='Apellido'></input><br />
                                            <input id='dni1' name='dni1' placeholder='DNI'></input>
                                        </div>
                                    </form>
                                    <button className='boton' type='submit' style={{ marginBottom: 40 }} onClick={sendDatos} >Comprar</button>
                                </div>
                            </>
                            : getCookieValue('sess') !== '' ?
                            <>
                            <p style={{paddingBottom: 40}}>Agotadas.</p>
                            </>
                            :
                            <>
                            </>
                        }
                    </div>
                </Container>
            </>
        )
    } else {
        return (
            <Container className='home'>
                <div className='eventos'>
                    <Spinner animation="border" style={{ marginBottom: 40 }} />
                </div>
            </Container>
        )
    }
}
export default Evento;