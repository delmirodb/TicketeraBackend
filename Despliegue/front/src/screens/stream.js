import { useEffect, useState } from "react";
import { Container, Spinner } from "react-bootstrap"
import { useParams } from "react-router-dom";
import { api } from "../API/api";

function Stream() {

    const getCookieValue = (name) => (
        document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)')?.pop() || ''
    )

    let data = useParams();
    let id = data.id;
    let token = getCookieValue('sess');

    const [stream, setStream] = useState(false);
    const [evento, setEvento] = useState(false);

    const getStream = async () => {
        await api.validarAcceso(id, token)
            .then(response => {
                setStream(true)
            }).catch(error => {
                window.location.href = "/";
                console.log(error)
            })
    }

    const getEvento = async () => {
        await api.getEvento(id)
            .then(response => {
                setEvento(response.data);
            }).catch(error => {
                window.location.href = "/";
                console.log(error)
            })
    }

    useEffect(() => {
        async function showStream() {
            getStream();
            getEvento();
        }
        showStream()
    }, [])

    if (stream) {
        return (
            <Container style={{ marginTop: 40, marginBottom: 40 }}>
                <h2>{evento.nombre}</h2>
                <iframe src="http://localhost:8080/players/hls.html" width={1000} height={450} />
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

export default Stream