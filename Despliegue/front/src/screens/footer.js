function Footer(){

return(
    <div className="footer-basic">
        <footer>
            <div className="social"><a href="#"><i className="bi bi-instagram"></i></a><a href="#"><i className="bi bi-twitter"></i></a><a href="#"><i className="bi bi-facebook"></i></a></div>
            <ul className="list-inline">
                <li className="list-inline-item"><a href="/">Home</a></li>
                <li className="list-inline-item"><a href="/eventos">Eventos</a></li>
                <li className="list-inline-item"><a href="/sobre-nosotros">Sobre nosotros</a></li>
            </ul>
            <p className="copyright">Cómpralas © 2022</p>
        </footer>
    </div>
)

}

export default Footer;