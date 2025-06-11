import { useEffect, useState } from 'react';
import { Navbar, Nav, Container, NavDropdown, Modal, Button } from 'react-bootstrap';
import notificationIcon from '../assets/notification.svg';
import { getUnreadNotifications, readNotification } from '../connection';
import DateTime from './DateTime';

const NavigationBar = () => {
  const user = JSON.parse(localStorage.getItem('user') as string);
  const pathname = location.pathname;
  const [notifications, setNotifications] = useState<object[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [intervalState, setIntervalState] = useState<any>();

  useEffect(() => {
    const fetchNotifications = async () => {
      const data = await getUnreadNotifications();
      setNotifications(data);
      
      setIntervalState(setInterval(async () => {
        const data = await getUnreadNotifications();
        if(data.length) {
          setNotifications([...data, ...notifications]);
        }
      }, 1000));
    }
    fetchNotifications();

    return () => {
        clearInterval(intervalState);
    };
  }, []);

  const handleShow = () => setShowModal(true);
  const handleClose = () => setShowModal(false);

  const handleRead = async (notification : any) => {
    if(await readNotification(notification.id)) {
      const data = await getUnreadNotifications();
      setNotifications(data);
    }
  }

  const logout = () => {
    localStorage.clear();
    location.reload();
  }

  return (
    <>
      <Navbar bg="dark" variant="dark" expand="sm" > 
        <Container>
          <Navbar.Brand href='/'>SZZDMSP</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link onClick={handleShow} className={pathname === '/notifications' ? 'active' : ''}>
              <img src={notificationIcon} alt="Powiadomienia" style={{ width: '20px', marginRight: '5px' }} />
              <span className="badge bg-secondary">{notifications.length}</span>
            </Nav.Link>
          </Nav>
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
          {!notifications.length && 'Brak nowych powiadomień.'}
          {notifications.map((notification: any) => (
            <div key={notification.id} className="notification-item p-3 mb-2 rounded border border-primary">
                <p className="mb-1" dangerouslySetInnerHTML={{ __html: notification.content }}></p>
              <p className="mb-2" style={{ fontSize: '0.8rem', textAlign: "right" }}><DateTime date={notification.createdAt} /></p>
              <div className="d-flex justify-content-end">
                <Button variant="primary" size="sm" className="me-2" onClick={() => handleRead(notification)}>Oznacz jako przeczytane</Button>
                <a href={`/teams/${notification.teamId}`}><Button variant="primary" size="sm">Przejdź do zespołu</Button></a>
              </div>
            </div>
          ))}
        </Modal.Body>
        <Modal.Footer className="bg-dark text-white">
          <a href="/notifications"><Button variant="primary">zobacz wszystkie</Button></a>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default NavigationBar;