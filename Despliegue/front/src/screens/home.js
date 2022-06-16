import React, { useEffect, useState } from 'react'
import { Card, Container, Row, Col, Spinner } from 'react-bootstrap'
import { api, url_api } from '../API/api';

function Home() {

    const [eventos, setEventos] = useState(false);

    const getEventos = async () => {
        await api.homeEventos()
            .then(response => {
                setEventos(response.data);
            })
    }

    useEffect(() => {
        async function showEventos() {
            getEventos();
        }
        showEventos()
    }, [])

    if (eventos) {
        return (
            <Container className='home'>
                <div style={{ marginBottom: 40 }}>
                    <h2><b>Los mejores eventos</b></h2>
                </div>
                <div className='eventos'>
                    <Row xl={3} l={3} md={2} s={2} xs={1}>
                        {eventos.map((value, index) => {
                            return (
                                <a key={index} href={'/evento/' + value.id}>
                                    <Col style={{ marginBottom: 40 }}>
                                        <Card key={index}>
                                            <Card.Img className='imagenesHome' variant="top" src={url_api + '/img/evento_' + value.id + '.jpg'} />
                                            <Card.Body>
                                                <Card.Title><h4>{value.nombre}</h4></Card.Title>
                                                <Card.Text>
                                                    <span className='tituloCiudad'>{value.ciudad}</span><br />
                                                    {value.descripcion}
                                                </Card.Text>
                                            </Card.Body>
                                        </Card>
                                    </Col>
                                </a>
                            )
                        })}
                    </Row>
                </div>
            </Container>
        )
    } else {
        return (
            <Container className='home'>
                <div style={{ marginBottom: 40 }}>
                    <h2><b>Los mejores eventos</b></h2>
                </div>
                <div className='eventos' style={{ marginBottom: 40 }}>
                    <Spinner animation="border" />
                </div>
            </Container>
        )
    }


}
export default Home;