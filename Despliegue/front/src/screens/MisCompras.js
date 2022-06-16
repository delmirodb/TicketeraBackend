import { useEffect, useState } from "react";
import { Container, Spinner, Table } from "react-bootstrap"
import { api } from "../API/api";

function MisCompras() {

    const getCookieValue = (name) => (
        document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)')?.pop() || ''
    )

    let token = getCookieValue('sess');

    const [compras, setCompras] = useState(false)

    const getCompras = async () => {
        await api.getComprasUsuario(token)
            .then(response => {
                setCompras(response.data);
                console.log(response.data)
            }).catch(error => {
                window.location.href = "/";
                console.log(error)
            })
    }

    useEffect(() => {
        async function showCompras() {
            getCompras();
        }
        showCompras()
    }, [])


    if (compras) {
        return (
            <Container style={{ textAlign: 'center' }}>
                <h2 style={{ marginTop: 40 }}>Tus compras</h2>
                {compras != '' ?
                    <Table className="tablaCompras">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Evento</th>
                        <th>Importe</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {compras.map((value, index) => {
                        return (
                            <tr key={index}>
                                <td>#{value[0]}</td>
                                <td>{value[1]}</td>
                                <td>{value[2]}€</td>
                                <td><a href={"/compra/" + value[0]}><button className="boton">Ver compra</button></a></td>
                            </tr>
                        )
                    })}
                    </tbody>
                </Table>
                :
                <div style={{ marginBottom: 40 }}>Parece que no tienes ninguna compra...</div>
                }
                
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

export default MisCompras