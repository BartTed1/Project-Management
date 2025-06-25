import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getTask } from "../connection";
import { Alert, Card, Col, Container, Row } from "react-bootstrap";
import DateTime from "../components/DateTime";
import Status from "../components/Status";
import Priority from "../components/Priority";
import deleteIcon from '../assets/delete.svg';
import editIcon from '../assets/edit.svg';
import checkIcon from '../assets/check.svg';
import cancelIcon from '../assets/cancel.svg';
import ReactMarkdown from 'react-markdown';

const Task = () => {
    const [alertMsg, setAlertMsg] = useState(null as any);
    const userLogged = JSON.parse(localStorage.getItem('user') as string)
    const { id } = useParams<{ id: string }>();
    const [data, setData] = useState(null as any);
    const [error, setError] = useState(null as any);

    useEffect(() => {
        const fetchTask = async () => {
          if (id) {
            const data = await getTask(id);
            console.log(data);
            if (data.error) setError(data.error);
            else setData(data);
          }
        };
        fetchTask();
    }, [id]);

    const handleDeleteTask = async (taskId: number) => {
        // if(window.confirm('Czy na pewno chcesz usunąć zadanie?')){
        //     if(await deleteTask(taskId)){
        //         setAlertMsg({
        //             variant: 'success',
        //             msg: 'pomyślnie usunięto zadanie'
        //         });
        //         const data = await getTask(id as string);
        //         setData(data);
        //     }else{
        //         setAlertMsg({
        //             variant: 'danger',
        //             msg: 'Coś poszło nie tak przy próbie usunięcia zadania'
        //         });
        //     }
        // }
    }

    const handleConfirmTask = async (taskId: number) => {
        // if(window.confirm('Czy na pewno chcesz zakończyć zadanie?')){
        //     if(await updateStatusTask(taskId, 'COMPLETED')){
        //         setAlertMsg({
        //             variant: 'success',
        //             msg: 'pomyślnie zakończono zadanie'
        //         });
        //         const data = await getTask(id as string);
        //         setData(data);
        //     }else{
        //         setAlertMsg({
        //             variant: 'danger',
        //             msg: 'Coś poszło nie tak przy próbie zakończenia zadania'
        //         });
        //     }
        // }
    }

    const handleCancelTask = async (taskId: number) => {
        // if(window.confirm('Czy na pewno chcesz zakończyć zadanie?')){
        //     if(await updateStatusTask(taskId, 'UNCOMPLETED')){
        //         setAlertMsg({
        //             variant: 'success',
        //             msg: 'pomyślnie zakończono zadanie'
        //         });
        //         const data = await getTask(id as string);
        //         setData(data);
        //     }else{
        //         setAlertMsg({
        //             variant: 'danger',
        //             msg: 'Coś poszło nie tak przy próbie zakończenia zadania'
        //         });
        //     }
        // }
    }

    if (!data) return <>{alertMsg && <Alert variant={alertMsg.variant}>{alertMsg.msg}</Alert>}</>;

    if (error) return <div>{error}</div>;

    return (
        <Container className="my-4">
            <Row className="justify-content-center">
                <Col md={8}>
                    <Card bg="dark" text="white">
                        <Card.Header as="h1">Szczegóły zadania</Card.Header>
                        <Card.Body>
                            {(userLogged.id === data.user.id || (userLogged.id === data.team.owner.id && data.status === 'DURING')) && (
                                <Card.Text>
                                    {userLogged.id === data.team.owner.id && (
                                        <>
                                            <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDeleteTask(data.id)}/>
                                            <a href={`/tasks/${data.id}/edit`}><img src={editIcon} alt='edit' width='20px' title='edytuj' /></a>
                                        </>    
                                    )}
                                    {userLogged.id === data.team.owner.id && data.status === 'DURING' && (
                                        <>
                                            <img src={checkIcon} alt='check' width='20px' title='zakończ pozytywnie' className='icon' onClick={() => handleConfirmTask(data.id)} />
                                            <img src={cancelIcon} alt='cancel' width='20px' title='zakończ negatywnie' className='icon' onClick={() => handleCancelTask(data.id)} />
                                        </>
                                    )}
                                </Card.Text>
                            )}
                            <Card.Text>
                                <strong>Id:</strong> {data.id}
                            </Card.Text>
                            <Card.Text>
                                <strong>tytuł:</strong> {data.title}
                            </Card.Text>
                            <Card.Text>
                                <strong>Nazwa zespołu:</strong> {data.team.name}
                            </Card.Text>
                            <Card.Text>
                                <strong>wykonawca:</strong> {`${data.user.name} <${data.user.email}>`}
                            </Card.Text>
                            <Card.Text>
                                <strong>utworzony:</strong> <DateTime date={data.createdAt} />
                            </Card.Text>
                            <Card.Text>
                                <strong>zaktualizowany:</strong> <DateTime date={data.updatedAt} />
                            </Card.Text>
                            <Card.Text>
                                <strong>termin:</strong> <DateTime date={data.deadline} />
                            </Card.Text>
                            {data.endAt && (
                                <Card.Text>
                                    <strong>zakończony:</strong> <DateTime date={data.endAt} />
                                </Card.Text>
                            )}
                            <Card.Text>
                                <strong>status:</strong> <Status status={data.status} />
                            </Card.Text>
                            <Card.Text>
                                <strong>priorytet</strong> <Priority priority={data.priority} />
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card bg="dark" text="white" className="mt-4">
                        <Card.Body>
                            <div className="markdown">
                                <ReactMarkdown>{data.description}</ReactMarkdown>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    )
}

export default Task