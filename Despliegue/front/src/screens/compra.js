import { useEffect, useState } from "react";
import { Container, Spinner } from "react-bootstrap"
import { useParams } from "react-router-dom";
import { api } from "../API/api";

function Compra(){

    const getCookieValue = (name) => (
        document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)')?.pop() || ''
    )

    let data = useParams();
    let id = data.id;
    let token = getCookieValue('sess');

    const [compra, setCompra] = useState(false);
    const [ticketCount, setCount] = useState(false);

    const getCompra = async () => {
        await api.getCompra(id,token)
            .then(response => {
                setCount(Object.keys(response.data.Tickets).length)
                setCompra(response.data);
            }).catch(error => {
                window.location.href = "/";
                console.log(error)
            })
    }

    useEffect(() => {
        async function showCompra() {
            getCompra();
        }
        showCompra()
    }, [])

if(compra){
    return(
        <Container>
            <h2 style={{ marginTop: 40 }}>Gracias por tu compra!</h2>
            <p><b>Número de referencia:</b> #{compra.Compra.id}</p>
            <p>
                <b>Evento: </b>{compra.Tickets[0].evento.nombre} - {compra.Tickets[0].evento.fecha} - {compra.Tickets[0].evento.ciudad}<br/>
                <b>Nº de entradas: </b> {ticketCount}<br/>
                <b>Total: </b> {compra.Compra.importe}€
            </p>
            <p><b>¿No encuentras las entradas en tu correo?</b></p>
            <button className="boton">Reenviar entradas</button>
            <div className="entradas" style={{ marginBottom: 40 }}>
            {compra.Tickets.map((value, index) => {
                            return (
                                <div key={index} className="entrada">
                                    <h5><b>Entrada Nº {index + 1}</b></h5>
                                    <p><b>ID</b> #{value.id}</p>
                                    <p>
                                        <b>Titular: </b> {value.propietario.nombre + " " + value.propietario.apellido}<br/>
                                        <b>DNI: </b> {value.propietario.dni}
                                    </p>
                                    <p>
                                        <b>Tipo de entrada: </b> {value.asiento.tipo.tipo}
                                    </p>
                                </div>
                            )
                        })}
            </div>
        </Container>
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
export default Compra