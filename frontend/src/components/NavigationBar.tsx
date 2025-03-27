import { useEffect, useState } from 'react';
import { Navbar, Nav, Container, NavDropdown, Modal, Button } from 'react-bootstrap';

const NavigationBar = () => {
    const user = JSON.parse(localStorage.getItem('user') as string);
    const pathname = location.pathname;

    const logout = () => {
        localStorage.clear();
        location.reload();
      }

    return (
        <>
          <Navbar bg="dark" variant="dark" expand="sm" > 
            <Container>
              <Navbar.Brand href='/'>SZZDMSP</Navbar.Brand>

              <Navbar.Toggle />
              <Navbar.Collapse>
                <Nav className="ms-auto">
                  <Nav.Link href='/' className={pathname === '/' ? 'active' : ''}>start</Nav.Link>
                  <Nav.Link href='/users' className={pathname === '/users' ? 'active' : ''}>użytkownicy</Nav.Link>
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
        </>
      );
}

export default NavigationBar;