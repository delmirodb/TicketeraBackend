import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import PruebaComponent from './screens/pruebaComponent';

import { BrowserRouter as Router, Route } from 'react-router-dom';
import NavBar from './screens/navbar';
import Home from './screens/home';
import { Redirect } from 'react-router-dom';
import { Switch } from 'react-router-dom';
import Evento from './screens/evento';
import Compra from './screens/compra';
import Stream from './screens/stream';
import MisCompras from './screens/MisCompras';
import Footer from './screens/footer';
import Validar from './screens/validar';

function App() {
  return (
    <Router className="App">
      <Switch>
        <Route exact path="/">
          <NavBar />
          <Home />
          <Footer />
        </Route>
        <Route exact path="/eventos">
          <NavBar />
          <PruebaComponent />
          <Footer />
        </Route>
        <Route exact path="/evento/:id">
          <NavBar />
          <Evento />
          <Footer />
        </Route>
        <Route exact path="/stream/:id">
          <NavBar />
          <Stream />
          <Footer />
        </Route>
        <Route exact path="/compra/:id">
          <NavBar />
          <Compra />
          <Footer />
        </Route>
        <Route exact path="/cuenta/compras">
          <NavBar />
          <MisCompras />
          <Footer />
        </Route>
        <Route exact path="/validar/:id">
          <NavBar />
          <Validar />
          <Footer />
        </Route>
        <Route exact path="/sobre-nosotros">
          <NavBar />
          <PruebaComponent />
          <Footer />
        </Route>
        <Route>
          <Redirect to="/" />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
