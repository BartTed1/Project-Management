import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import { addTeamUser, createFile, createMessage, deleteFile, deleteTask, deleteTeam, getMessages, getOldMessages, getRaport, getTeam, getUserByEmail, refreshMessages, removeTeamUser, updateStatusTask } from "../connection";
import { Alert, Card, Col, Container, Row, Tab, Table, Tabs, Form, Button } from "react-bootstrap";
import infoIcon from '../assets/info.svg';
import deleteIcon from '../assets/delete.svg';
import editIcon from '../assets/edit.svg';
import checkIcon from '../assets/check.svg';
import cancelIcon from '../assets/cancel.svg';
import downloadIcon from '../assets/download.svg';
import Status from "../components/Status";
import Priority from "../components/Priority";
import DateTime from "../components/DateTime";
import "../style/chat.css";
import { format } from 'date-fns';

const Team = () => {
    const [alertMsg, setAlertMsg] = useState(null as any);
    const [userAlertMsg, setUserAlertMsg] = useState(null as any);
    const userLogged = JSON.parse(localStorage.getItem('user') as string)
    const { id } = useParams<{ id: string }>();
    const [data, setData] = useState(null as any);
    const [error, setError] = useState(null as any);
    const [messages, setMessages] = useState(null as any);
    const chatBoxRef = useRef<HTMLDivElement>(null);
    const refreshInterval = useRef<any>(null);
    const [isrefres, setIsrefres] = useState(false);
    const [isOlderMessages, setIsOlderMessages] = useState(true);

    useEffect(() => {
        const fetchTeams = async () => {
          if (id) {
            const data = await getTeam(id);
            if (data.error) setError(data.error);
            else setData(data);
          }
        };
        fetchTeams();
    }, [id]);

    useEffect(() => {
        if (chatBoxRef.current) {
            chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
        }
    }, [messages]);

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
        if((await getUserByEmail(email)) == 404){
            setUserAlertMsg({
                variant: 'danger',
                msg: 'Użytkownik o podanym emailu nie istnieje'
            });
            return;
        }

        if(await addTeamUser(id as string, email)){
            setUserAlertMsg({
                variant: 'success',
                msg: 'pomyślnie dodano użytkownika do zespołu'
            });
            const data = await getTeam(id as string);
            setData(data);
        }else{
            setUserAlertMsg({
                variant: 'danger',
                msg: 'Coś poszło nie tak przy próbie dodania użytkownika do zespołu'
            });
        }
    }

    const handleDeleteUser = async (userId: number) => {
        if(window.confirm('Czy na pewno chcesz usunąć użytkownika z zespołu?')){
            if(await removeTeamUser(id as string, userId)){
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

    const handleDeleteTask = async (taskId: number) => {
        if(window.confirm('Czy na pewno chcesz usunąć zadanie?')){
            if(await deleteTask(taskId)){
                setAlertMsg({
                    variant: 'success',
                    msg: 'pomyślnie usunięto zadanie'
                });
                const data = await getTeam(id as string);
                setData(data);
            }else{
                setAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie usunięcia zadania'
                });
            }
        }
    }

    const handleConfirmTask = async (taskId: number) => {
        if(window.confirm('Czy na pewno chcesz zakończyć zadanie?')){
            if(await updateStatusTask(taskId, 'COMPLETED')){
                setAlertMsg({
                    variant: 'success',
                    msg: 'pomyślnie zakończono zadanie'
                });
                const data = await getTeam(id as string);
                setData(data);
            }else{
                setAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie zakończenia zadania'
                });
            }
        }
    }

    const handleCancelTask = async (taskId: number) => {
        if(window.confirm('Czy na pewno chcesz zakończyć zadanie?')){
            if(await updateStatusTask(taskId, 'UNCOMPLETED')){
                setAlertMsg({
                    variant: 'success',
                    msg: 'pomyślnie zakończono zadanie'
                });
                const data = await getTeam(id as string);
                setData(data);
            }else{
                setAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie zakończenia zadania'
                });
            }
        }
    }

    const handleDeleteFile = async (fileId: number) => {
        if(window.confirm('Czy na pewno chcesz usunąć plik?')){
            if(await deleteFile(fileId)){
                setAlertMsg({
                    variant: 'success',
                    msg: 'pomyślnie usunięto plik'
                });
                const data = await getTeam(id as string);
                setData(data);
            }else{
                setAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie usunięcia pliku'
                });
            }
        }
    }

    const handleCreateFile = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const fileInput = e.currentTarget.elements[0] as HTMLInputElement;
        if (!fileInput.files || fileInput.files.length === 0) {
            setAlertMsg({
                variant: 'danger',
                msg: 'Nie wybrano pliku'
            });
            return;
        }
        const file = fileInput.files[0];
        
        if(await createFile(Number(id), file)){
            setAlertMsg({
                variant: 'success',
                msg: 'pomyślnie dodano plik'
            });
            const data = await getTeam(id as string);
            setData(data);
        }else{
            setAlertMsg({
                variant: 'danger',
                msg: 'Coś poszło nie tak przy próbie dodania pliku'
            });
        }
    }

    const handleEnteredMessage = async () => {
        if (id && !messages) {
            const data = await getMessages(Number(id), 10, 1);
            if (data.error) {
                setError("wystąpił błąd przy pobieraniu wiadomości");
            } else {
                setIsrefres(true);
                setMessages(data.messages.reverse());
            }
        }
    }
    
    if(isrefres){
        setIsrefres(false);
        clearInterval(refreshInterval.current);
        refreshInterval.current = setInterval(async () => {
            const lastMessageId = messages ? messages[messages.length - 1].id : 0;
            const newMessages = await refreshMessages(Number(id), lastMessageId);
            if (newMessages.error) {
                setError("wystąpił błąd przy pobieraniu wiadomości");
            }else if(newMessages.length > 0){
                setMessages([...messages, ...newMessages.reverse()]);
            }
        }, 1000);
    }

    const handleSendMessage = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const messageInput = e.currentTarget.elements[0] as HTMLInputElement;
        await createMessage(Number(id), messageInput.value)
        messageInput.value = '';
    }

    const handeScroll = async (e: any) => {
        if(e.target.scrollTop === 0){
            if(isOlderMessages){
                const oldestMessageId = messages ? messages[0].id : 0;
                const newMessages = await getOldMessages(Number(id), oldestMessageId, 10);
                if (newMessages.error) {
                    setError("wystąpił błąd przy pobieraniu wiadomości");
                }else if(newMessages.length > 0){
                    setMessages([...newMessages.reverse(), ...messages]);
                }else{
                    setIsOlderMessages(false);
                }
            }
        }
    }

    const handlegenaratereport = async () => {
        const response = await getRaport(Number(id));

        if (response) {
            const blob = new Blob([response], { type: 'application/pdf' });
            const url = window.URL.createObjectURL(blob);
            const date = format(new Date(), 'dd-MM-yyyy-HH-mm');
            const a = document.createElement('a');
            a.href = url;
            a.download = `${data.name}-${date}.pdf`;
            a.click();
            window.URL.revokeObjectURL(url);
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
                                        <Button onClick={handlegenaratereport}>Generuj raport</Button>
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
                                                    <tr key={user.user.id}>
                                                        <td>{user.user.id}</td>
                                                        <td>{user.user.name}</td>
                                                        <td>{user.user.email}</td>
                                                        <td>
                                                            <a href={`/users/${user.user.id}`}><img src={infoIcon} alt='info' width='20px' title='szczegóły' /></a>
                                                            {userLogged.id === data.owner.id && user.user.id != userLogged.id && (
                                                                <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDeleteUser(user.user.id)}/>   
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
                                    <a href={`/teams/${id}/addTask`} className='btn btn-primary mb-4'>Utwórz Zadanie</a>
                                    <div className="table-responsive">
                                        <Table striped bordered hover variant='dark'>
                                            <thead className='thead text-center'>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Tytuł</th>
                                                    <th>Status</th>
                                                    <th>Priorytet</th>
                                                    <th>Termin</th>
                                                    <th>Oddany</th>
                                                    <th>Wykonawca</th>
                                                    <th>Akcje</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {data.Task.map((task: any) => (
                                                    <tr key={task.id}>
                                                        <td>{task.id}</td>
                                                        <td>{task.title}</td>
                                                        <td><Status status={task.status} /></td>
                                                        <td><Priority priority={task.priority} /></td>
                                                        <td><DateTime date={task.deadline} /></td>
                                                        <td>{task.endAt ? <DateTime date={task.endAt} /> : "-"}</td>
                                                        <td>{task.user.name}</td>
                                                        <td>
                                                            <a href={`/tasks/${task.id}`}><img src={infoIcon} alt='info' width='20px' title='szczegóły' /></a>
                                                            {userLogged.id === data.owner.id && (
                                                                <>
                                                                    <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDeleteTask(task.id)}/>
                                                                    <a href={`/tasks/${task.id}/edit`}><img src={editIcon} alt='edit' width='20px' title='edytuj' /></a>
                                                                </>
                                                            )}
                                                            {task.status === 'DURING' && (task.user.id === userLogged.id || userLogged.id === data.owner.id) && (
                                                                <>
                                                                    <img src={checkIcon} alt='check' width='20px' title='zakończ pozytywnie' className='icon' onClick={() => handleConfirmTask(task.id)} />
                                                                    <img src={cancelIcon} alt='cancel' width='20px' title='zakończ negatywnie' className='icon' onClick={() => handleCancelTask(task.id)} />
                                                                </>
                                                            )}
                                                        </td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </Table>
                                    </div>
                                </Card.Body>
                            </Tab>
                            <Tab eventKey="your-tasks" title="Twoje zadania">
                                <Card.Body>
                                    <a href={`/teams/${id}/addTask`} className='btn btn-primary mb-4'>Utwórz Zadanie</a>
                                    <div className="table-responsive">
                                        <Table striped bordered hover variant='dark'>
                                            <thead className='thead text-center'>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Tytuł</th>
                                                    <th>Status</th>
                                                    <th>Priorytet</th>
                                                    <th>Termin</th>
                                                    <th>Oddany</th>
                                                    <th>Akcje</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {data.Task.map((task: any) => (
                                                    task.user.id === userLogged.id &&
                                                    <tr key={task.id}>
                                                        <td>{task.id}</td>
                                                        <td>{task.title}</td>
                                                        <td><Status status={task.status} /></td>
                                                        <td><Priority priority={task.priority} /></td>
                                                        <td><DateTime date={task.deadline} /></td>
                                                        <td>{task.endAt ? <DateTime date={task.endAt} /> : "-"}</td>
                                                        <td>
                                                            <a href={`/tasks/${task.id}`}><img src={infoIcon} alt='info' width='20px' title='szczegóły' /></a>
                                                            {userLogged.id === data.owner.id && (
                                                                <>
                                                                    <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDeleteTask(task.id)}/>
                                                                    <a href={`/tasks/${task.id}/edit`}><img src={editIcon} alt='edit' width='20px' title='edytuj' /></a>
                                                                </>
                                                            )}
                                                            {task.status === 'DURING' && (task.user.id === userLogged.id || userLogged.id === data.owner.id) && (
                                                                <>
                                                                    <img src={checkIcon} alt='check' width='20px' title='zakończ pozytywnie' className='icon' onClick={() => handleConfirmTask(task.id)} />
                                                                    <img src={cancelIcon} alt='cancel' width='20px' title='zakończ negatywnie' className='icon' onClick={() => handleCancelTask(task.id)} />
                                                                </>
                                                            )}
                                                        </td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </Table>
                                    </div>
                                </Card.Body>
                            </Tab>
                            <Tab eventKey="files" title="Pliki">
                                <Card.Body>
                                <Form className="mb-4" encType="multipart/form-data" onSubmit={handleCreateFile}>
                                    <Row>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label>Dodaj plik</Form.Label>
                                                <Form.Control type='file' name='file' />
                                            </Form.Group>
                                        </Col>
                                        <Col md={2} className="d-flex align-items-end">
                                            <Button variant="primary" type="submit" className="w-100">
                                                Dodaj
                                            </Button>
                                        </Col>
                                    </Row>
                                </Form>
                                <div className="table-responsive-sm">
                                    <Table striped bordered hover variant='dark'>
                                        <thead className='thead text-center'>
                                            <tr>
                                                <th>#</th>
                                                <th>Nazwa</th>
                                                <th>Dodał</th>
                                                <th>Utworzony</th>
                                                <th>Akcje</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {data.File.map((file: any) => (
                                                <tr key={file.id}>
                                                    <td>{file.id}</td>
                                                    <td>{file.name}</td>
                                                    <td>{`${file.user.name} <${file.user.email}>`}</td>
                                                    <td><DateTime date={file.createdAt} /></td>
                                                    <td>
                                                        <a href={`${file.path}`} target="_blank"><img src={downloadIcon} alt='download' width='20px' title='pobierz' /></a>
                                                        {(userLogged.id === data.owner.id || userLogged.id === file.user.id) && (
                                                            <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDeleteFile(file.id)}/>
                                                        )}
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </Table>
                                </div>
                                </Card.Body>
                            </Tab>
                            <Tab eventKey="message" title="Czat" onEntered={handleEnteredMessage}>
                                <Card.Body>
                                    <Form onSubmit={handleSendMessage}>
                                    <Row>
                                        <Col>
                                            <Form.Group>
                                                <Form.Control type='text' name='messageInput' placeholder="wiadomość" />
                                            </Form.Group>
                                        </Col>
                                        <Col md={2} className="d-flex align-items-end">
                                            <Button variant="primary" type="submit" className="w-100">
                                                Wyślij
                                            </Button>
                                        </Col>
                                    </Row>
                                    </Form>
                                    {messages &&
                                        <div className="chat-box" ref={chatBoxRef} onScroll={handeScroll}>
                                            {messages.map((message: any) => (
                                                <div key={message.id} className={"chat-message" + (message.user.id === userLogged.id ? " my-message" : "")}>
                                                    <strong>{message.user.id === userLogged.id ? "Ty" :  message.user.name}:</strong> {message.content}
                                                    <div className="date-time"><DateTime date={message.createdAt} /></div>
                                                </div>
                                            ))}
                                        </div>
                                    }
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