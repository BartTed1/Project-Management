import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { deleteUser, getUser, resetPassword } from '../connection';
import { Container, Card, Row, Col, Alert, Table } from 'react-bootstrap';

import deleteIcon from '../assets/delete.svg';
import restoreIcon from '../assets/restore.svg';
import editIcon from '../assets/edit.svg';

const User = () => {
  const [alertMsg, setAlertMsg] = useState(null as any);
  const userLogged = JSON.parse(localStorage.getItem('user') as string)
  const { id } = useParams<{ id: string }>();
  const [data, setData] = useState(null as any);
  const [error, setError] = useState(null as any);

  useEffect(() => {
    const fetchUser = async () => {
      if (id) {
        const data = await getUser(id);
        if (data.error) setError(data.error);
        else setData(data);
      }
    };
    fetchUser();
  }, [id]);

  const handleDelete = async (id: number) => {
    if(window.confirm('Czy na pewno chcesz usunąć użytkownika?')){
        if(await deleteUser(id.toString())){
            setAlertMsg({
                variant: 'success',
                msg: 'pomyślnie usunięto użytkownika'
            });
            setData(null);
        }else{
            setAlertMsg({
                variant: 'danger',
                msg: 'Coś poszło nie tak przy próbie usunięcia użytkownika'
            });
        }
    }
}

const handleRestore = async (id: number) => {
    if(window.confirm('Czy na pewno chcesz zresetować hasło użytkownika?')){
        const password = await resetPassword(id.toString())
        if(password){
            setAlertMsg({
                variant: 'success',
                msg: `pomyślnie zresetowano hasło użytkownika nowe hasło to: ${password}`
            });
        }else{
            setAlertMsg({
                variant: 'danger',
                msg: 'Coś poszło nie tak przy próbie zresetowania hasła użytkownika'
            });
        }
    }
}

  if (!data) return <>{alertMsg && <Alert variant={alertMsg.variant}>{alertMsg.msg}</Alert>}</>;

  if (error) return <div>{error}</div>;

  return (
    <Container className="my-4">
      <Row className="justify-content-center">
        {((userLogged.role != 'USER' && data.role == 'USER') || (userLogged.role == 'SUPERADMIN' && data.role == 'ADMIN')) &&
          <Col md={8}>
            {alertMsg && <Alert variant={alertMsg.variant}>{alertMsg.msg}</Alert>}
            <Card bg="dark" text="white" className="mb-4 text-center">
              <Card.Header as="h1">Zarządzanie Użytkownikiem</Card.Header>
              <Card.Body>
                <Card.Text>
                  <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDelete(data.id)}/>
                  <img src={restoreIcon} alt='res' width='20px' title='zresetuj hasło' className='icon' onClick={() => handleRestore(data.id)} />
                  <a href={`/users/${data.id}/edit`}><img src={editIcon} alt='edit' width='20px' title='edytuj' /></a>
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
        }
        <Col md={8}>
          <Card bg="dark" text="white">
            <Card.Header as="h1">Szczegóły Użytkownika</Card.Header>
            <Card.Body>
              <Card.Text>
                <strong>Nazwa:</strong> {data.name}
              </Card.Text>
              <Card.Text>
                <strong>Email:</strong> {data.email}
              </Card.Text>
              <Card.Text>
                <strong>Rola:</strong> {data.role}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
        {data.teams.length > 0 && (
          <Col md={8}>
            <Card bg="dark" text="white" className="mt-4">
              <Card.Header as="h1">Zespoły</Card.Header>
              <Card.Body>
                <div className="table-responsive">
                  <Table striped bordered hover variant='dark'>
                  <thead className='thead text-center'>
                    <tr>
                    <th>#</th>
                    <th>Nazwa</th>
                    <th>opis</th>
                    <th>nazwa właściciela</th>
                    <th>email właściciela</th>
                    </tr>
                  </thead>
                  <tbody>
                    {data.teams.map((team: any) => (
                    <tr key={team.team.id}>
                      <td>{team.team.id}</td>
                      <td>{team.team.name}</td>
                      <td>{team.team.description}</td>
                      <td>{team.team.owner.name}</td>
                      <td>{team.team.owner.email}</td>
                    </tr>
                    ))}
                  </tbody>
                  </Table>
                </div>
              </Card.Body>
            </Card>
          </Col>
        )}
      </Row>
    </Container>
  );
};

export default User;