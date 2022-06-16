import React, { useEffect, useState } from 'react'
import { Navbar, Nav, Container, Modal, Button, NavDropdown } from 'react-bootstrap'
import { api } from '../API/api';

function NavBar() {
    const [showLogin, setShowLogin] = useState(false);
    const [showRegister, setShowRegister] = useState(false);

    const handleCloseLogin = () => setShowLogin(false);
    const handleShowLogin = () => setShowLogin(true);
    const handleCloseRegister = () => setShowRegister(false);
    const handleShowRegister = () => setShowRegister(true);

    const [logged, setLogged] = useState(false);

    const submitRegister = async () => {

        const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/

        var nombre = document.getElementById('nombre').value;
        var apellido = document.getElementById('apellido').value;
        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;

        if (nombre !== '' && apellido !== '' && emailRegex.test(email) && password !== '') {
            await api.register(nombre, apellido, email, password)
                .then(response => {
                    window.location.reload(false);
                }).catch(error => {
                    const errorRegister = document.getElementById('errorRegister');
                    errorRegister.innerHTML = 'Ya existe una cuenta con este correo electrónico.';
                    errorRegister.classList.add('errorLogin');
                })
        } else {
            const errorRegister = document.getElementById('errorRegister');
            errorRegister.innerHTML = 'Rellena todos los campos correctamente.';
            errorRegister.classList.add('errorLogin');
        }

    }

    useEffect(() => {
        async function loggedIn() {
            getLogged();
        }
        loggedIn()
    }, [])

    const getCookieValue = (name) => (
        document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)')?.pop() || ''
    )

    const getLogged = async () => {
        const token = getCookieValue('sess')

        if (token !== '') {
            await api.isLogged(token).then(response => {
                setLogged(response.data.id)
            }).catch(error => {
                setLogged(false)
                document.cookie = 'sess' + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
                window.location.reload(false);
            })
        }

    }

    function logOut(){
        document.cookie = 'sess' + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        window.location.reload(false);
    }

    const submitLogin = async () => {
        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;

        await api.login(email, password)
            .then(response => {
                document.cookie = 'sess=' + response.data + '; Path=/; max-age=3600';
                window.location.reload(false);
            }).catch(error => {
                const errorLogin = document.getElementById('errorLogin');
                errorLogin.innerHTML = 'Credenciales erróneas.';
                errorLogin.classList.add('errorLogin');
            })
    }

    return (
        <Navbar collapseOnSelect expand="lg" bg="light" variant="light" sticky='top'>
            <Container>
                <Navbar.Brand href="/"><img alt='Compralas' src='/img/compralas logo.png' width='60px' /></Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href="/">Home</Nav.Link>
                        <Nav.Link href="/eventos">Eventos</Nav.Link>
                        <Nav.Link href="/sobre-nosotros">Sobre nosotros</Nav.Link>
                    </Nav>
                    <Nav>
                        {getCookieValue('sess') === ''
                            ?
                            <>
                                <Nav.Link onClick={handleShowLogin} id="btnLogin">Iniciar sesión</Nav.Link>
                                <Nav.Link onClick={handleShowRegister} id="btnRegister">Regístrate</Nav.Link>
                            </>
                            :
                            <NavDropdown title="Mi cuenta" id="basic-nav-dropdown">
                                <NavDropdown.Item href="#"><i className="bi bi-person-circle"></i> Mis datos</NavDropdown.Item>
                                <NavDropdown.Item href="/cuenta/compras"><i className="bi bi-ticket-perforated"></i> Mis compras</NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item onClick={logOut}><i className="bi bi-door-open"></i> Cerrar sesión</NavDropdown.Item>
                            </NavDropdown>
                        }
                    </Nav>
                </Navbar.Collapse>
            </Container>

            <Modal show={showLogin} onHide={handleCloseLogin}>
                <Modal.Header closeButton>
                    <Modal.Title>Iniciar sesión</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="Login">
                        <form>
                            <label htmlFor="email">Email</label><br />
                            <input type="text" id="email" /><br />
                            <label htmlFor="password">Contraseña</label><br />
                            <input type="password" id="password" /><br /><br />
                            <div style={{ display: 'none' }} id='errorLogin'></div>
                        </form>
                        <Button onClick={submitLogin}>Acceder</Button>
                    </div>
                </Modal.Body>
            </Modal>

            <Modal show={showRegister} onHide={handleCloseRegister}>
                <Modal.Header closeButton>
                    <Modal.Title>Regístrate</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="Signup">
                        <form>
                            <label htmlFor="nombre">Nombre</label><br />
                            <input type="text" id="nombre" /><br />
                            <label htmlFor="apellido">Apellido</label><br />
                            <input type="text" id="apellido" /><br />
                            <label htmlFor="email">Email</label><br />
                            <input type="text" id="email" /><br />
                            <label htmlFor="password">Contraseña</label><br />
                            <input type="password" id="password" /><br /><br />
                            <div style={{ display: 'none' }} id='errorRegister'></div>
                        </form>
                        <Button onClick={submitRegister}>Registrar</Button>
                    </div>
                </Modal.Body>
            </Modal>
        </Navbar>
    )

}

export default NavBar;