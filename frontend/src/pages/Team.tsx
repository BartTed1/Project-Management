import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getTeam, addTeamMember, deleteTeam, removeTeamMember  } from "../connection";
import { Alert, Card, Col, Container, Row, Tab, Table, Tabs, Form, Button } from "react-bootstrap";
import infoIcon from '../assets/info.svg';
import deleteIcon from '../assets/delete.svg';
import editIcon from '../assets/edit.svg';
import ChatBox from "../components/ChatBox";

const Team = () => {
    const [alertMsg, setAlertMsg] = useState(null as any);
    const [userAlertMsg, setUserAlertMsg] = useState(null as any);
    const userLogged = JSON.parse(localStorage.getItem('user') as string)
    const { id } = useParams<{ id: string }>();
    const [data, setData] = useState(null as any);
    const [error, setError] = useState(null as any);

    const fetchTeams = async () => {
          if (id) {
            const data = await getTeam(id);
            if (data.error) setError(data.error);
            else setData(data);
          }
        };

    useEffect(() => { 
        fetchTeams();
    }, [id]);
    

    const handleDelete = async (id: number) => {
        if(window.confirm('Czy na pewno chcesz usunąć zespół?')){
            if(await deleteTeam(id.toString())){
                setAlertMsg({
                    variant: 'success',
                    msg: 'pomyślnie usunięto zespół'
                });
                setData(null);
            }else{
                setAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie usunięcia zespołu'
                });
            }
        }
    }

    const addUser = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const email = (e.currentTarget.elements[0] as HTMLInputElement).value;
        if(await addTeamMember(id as string, email, setUserAlertMsg)){
            fetchTeams();
        }

    }

    const handleDeleteUser = async (userId: number) => {
        if(window.confirm('Czy na pewno chcesz usunąć użytkownika z zespołu?')){
            if(await removeTeamMember(id as string, userId.toString())){
                setUserAlertMsg({
                    variant: 'success',
                    msg: 'pomyślnie usunięto użytkownika z zespołu'
                });
                const data = await getTeam(id as string);
                setData(data);
            }else{
                setUserAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie usunięcia użytkownika z zespołu'
                });
            }
        }
    }


    if (!data) return <>{alertMsg && <Alert variant={alertMsg.variant}>{alertMsg.msg}</Alert>}</>;

    if (error) return <div>{error}</div>;

    return (
        <Container className="my-4">
            <Row className="justify-content-center">
                <Col md={10}>
                    {alertMsg && <Alert variant={alertMsg.variant}>{alertMsg.msg}</Alert>}
                    <Card bg="dark" text="white">
                        <Card.Header as="h1">zespół: {data.name}</Card.Header>
                        <Tabs defaultActiveKey="details" id="team-tabs" variant="pills" className="p-2">
                            <Tab eventKey="details" title="Szczegóły">
                                <Card.Body>
                                    <Card.Text>
                                        <strong>Nazwa:</strong> {data.name}
                                    </Card.Text>
                                    <Card.Text>
                                        <strong>opis:</strong> {data.description}
                                    </Card.Text>
                                    <Card.Text>
                                        <strong>Nazwa właściciela:</strong> {data.owner.name}
                                    </Card.Text>
                                    <Card.Text>
                                        <strong>Email właściciela:</strong> {data.owner.email}
                                    </Card.Text>
                                    {userLogged.id === data.owner.id && (
                                        <Card.Text>
                                            <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDelete(data.id)}/>
                                            <a href={`/teams/${data.id}/edit`}><img src={editIcon} alt='edit' width='20px' title='edytuj' /></a>
                                        </Card.Text>
                                    )}
                                    <Card.Text>
                                        <Button >Generuj raport</Button>
                                    </Card.Text>
                                </Card.Body>
                            </Tab>
                            <Tab eventKey="members" title="Członkowie">
                                {userAlertMsg && <Alert variant={userAlertMsg.variant}>{userAlertMsg.msg}</Alert>}
                                <Card.Body>
                                    {userLogged.id === data.owner.id && (
                                        <Form className="mb-4" onSubmit={addUser}>
                                            <Row>
                                                <Col md={10}>
                                                    <Form.Group>
                                                        <Form.Label>Dodaj użytkownika do zespołu</Form.Label>
                                                        <Form.Control type='email' placeholder='email użytkownika' />
                                                    </Form.Group>
                                                </Col>
                                                <Col md={2} className="d-flex align-items-end">
                                                    <Button variant="primary" type="submit" className="w-100">
                                                        Dodaj
                                                    </Button>
                                                </Col>
                                            </Row>
                                        </Form>
                                    )}
                                    <div className="table-responsive">
                                        <Table striped bordered hover variant='dark'>
                                            <thead className='thead text-center'>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Nazwa</th>
                                                    <th>Email</th>
                                                    <th>Akcje</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {data.users.map((user: any) => (
                                                    <tr key={user.id}>
                                                        <td>{user.id}</td>
                                                        <td>{user.name}</td>
                                                        <td>{user.email}</td>
                                                        <td>
                                                            <a href={`/users/${user.id}`}><img src={infoIcon} alt='info' width='20px' title='szczegóły' /></a>
                                                            {userLogged.id === data.owner.id && user.id != userLogged.id && (
                                                                <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDeleteUser(user.id)}/>   
                                                            )}
                                                        </td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </Table>
                                    </div>
                                </Card.Body>
                            </Tab>
                            <Tab eventKey="tasks" title="Wszystkie zadania">
                                <Card.Body>
                                    Wielu spośród żyjących zasługuje na śmierć. A niejeden z tych, którzy umierają zasługuje na życie. Czy możesz ich nim obdarzyć? Nie bądź więc tak pochopny w ferowaniu wyroków śmierci, nawet bowiem najmądrzejszy nie wszystko wie.
                                </Card.Body>
                            </Tab>
                            <Tab eventKey="your-tasks" title="Twoje zadania">
                                <Card.Body>
                                   Czasem niebezpiecznie jest wyjść z domu, gdy staniesz na drodze, nigdy nie wiadomo, dokąd cię nogi poniosą
                                </Card.Body>
                            </Tab>
                            <Tab eventKey="files" title="Pliki">
                                <Card.Body>
                                    Nikt prawie nie wie, dokąd go zaprowadzi droga, póki nie stanie u celu.
                                </Card.Body>
                            </Tab>
                            <Tab eventKey="message" title="Czat">
                                <Card.Body>
                                    <ChatBox teamId={Number(id)} userId={userLogged.id} />
                                </Card.Body>
                            </Tab>
                        </Tabs>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}

export default Team;