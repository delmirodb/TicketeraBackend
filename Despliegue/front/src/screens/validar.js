import { useEffect, useState } from "react";
import { Container, Spinner } from "react-bootstrap";
import { useParams } from "react-router-dom";
import { api } from "../API/api";

function Validar(){

    const getCookieValue = (name) => (
        document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)')?.pop() || ''
    )

    let data = useParams();
    let id = data.id;
    let token = getCookieValue('sess');

    const [ticket, setTicket] = useState(false);

    const validarTicket = async () => {
        await api.validarTicket(id,token)
            .then(response => {
                setTicket(response.data);
            }).catch(error => {
                // window.location.href = "/";
                // console.log(error)
            })
    }

    useEffect(() => {
        async function showTicket() {
            validarTicket();
        }
        showTicket()
    }, [])

    return(
        <Container style={{marginBottom: 40, marginTop: 40}}>
        <h4>Ticket #{id}</h4>
        {ticket !== 'Utilizado' ?
        <h3 style={{color: 'green'}}>Ticket validado.</h3>
        :
        <h3 style={{color: 'red'}}>Este ticket ya fue utilizado.</h3>
        }
        </Container>
        
    )
    

}

export default Validar;