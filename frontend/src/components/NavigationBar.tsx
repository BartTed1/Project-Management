import { Navbar, Nav, Container, NavDropdown, Modal, Button } from 'react-bootstrap';
import notificationIcon from '../assets/notification.svg';
import { useState } from 'react';

const NavigationBar = () => {
    const [showModal, setShowModal] = useState(false);
    const user = JSON.parse(localStorage.getItem('user') as string);
    const pathname = location.pathname;

    const logout = () => {
      localStorage.clear();
      location.reload();
    }

    const handleShow = () => setShowModal(true);
    const handleClose = () => setShowModal(false);

    return (
        <>
          <Navbar bg="dark" variant="dark" expand="sm" > 
            <Container>
              <Navbar.Brand href='/'>SZZDMSP</Navbar.Brand>
              <Nav className="me-auto">
              <Nav.Link onClick={handleShow} className={pathname === '/notifications' ? 'active' : ''}>
                <img src={notificationIcon} alt="Powiadomienia" style={{ width: '20px', marginRight: '5px' }} />
                <span className="badge bg-secondary">{0}</span>
              </Nav.Link>
            </Nav>
            <Navbar.Toggle />
              <Navbar.Toggle />
              <Navbar.Collapse>
                <Nav className="ms-auto">
                  <Nav.Link href='/' className={pathname === '/' ? 'active' : ''}>start</Nav.Link>
                  <Nav.Link href='/users' className={pathname === '/users' ? 'active' : ''}>użytkownicy</Nav.Link>
                  <NavDropdown title="zespoły" id="basic-nav-dropdown" menuVariant='dark'>
                    {user.role !== 'SUPERADMIN' && (
                      <>
                        <NavDropdown.Item href='/own-teams' className={pathname === '/own-teams' ? 'active' : ''}>Twoje zespoły</NavDropdown.Item>
                      </>
                    )}
                    <NavDropdown.Item href='/teams' className={pathname === '/teams' ? 'active' : ''}>wszystkie zespoły</NavDropdown.Item>
                  </NavDropdown>
                  <NavDropdown title={user.name} id="basic-nav-dropdown" menuVariant='dark'>
                    {user.role !== 'SUPERADMIN' && (
                      <>
                        <NavDropdown.Item href='/settings' className={pathname === '/settings' ? 'active' : ''}>Ustawienia</NavDropdown.Item>
                        <NavDropdown.Divider />
                      </>
                    )}
                    <NavDropdown.Item onClick={logout}>Wyloguj się</NavDropdown.Item>
                  </NavDropdown>
                </Nav>
              </Navbar.Collapse>
            </Container>
          </Navbar>

          <Modal show={showModal} onHide={handleClose}>
            <Modal.Header closeButton className="bg-dark text-white">
              <Modal.Title>Powiadomienia</Modal.Title>
            </Modal.Header>
            <Modal.Body className="bg-dark text-white">
              
            </Modal.Body>
          </Modal>
        </>
      );
}

export default NavigationBar;