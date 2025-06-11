import { useEffect, useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import { getUnreadNotifications, getUser, readNotification } from "../connection";
import DateTime from "../components/DateTime";
import Priority from "../components/Priority";

const Home = () => {
    const [notifications, setNotifications] = useState([]);
    const [user, setUser] = useState<{
        role: string; teams: any[], Task: any[] 
    } | null>(null);
    const userLogged = JSON.parse(localStorage.getItem('user') as string)

    useEffect(() => {
        const fetchUser = async () => {
            const response = await getUser(userLogged.id);
            setUser(response);
        }

        const fetchNotifications = async () => {
            const response = await getUnreadNotifications();
            setNotifications(response);
        };

        fetchUser();
        fetchNotifications();
    }, []);

    const handleRead = async (notification : any) => {
        if(await readNotification(notification.id)) {
          const data = await getUnreadNotifications();
          setNotifications(data);
        }
    }

    if (!user) {
        return <h1>Ładowanie</h1>;
    }

    if(user.role == "SUPERADMIN") {
        return <h1 className="text-center">Witaj w SZZDMSP</h1>;
    }

    return (
        <>
            <Container className="my-4">
                <Row className="justify-content-center">
                    <Col md={12}>
                        <h1 className="text-center">Witaj w SZZDMSP</h1>
                    </Col>
                </Row>
                <Row className="justify-content-center">
                    <Col md={12}>
                        <h2 className="text-center">Powiadomienia</h2>
                    </Col>
                </Row>
                <Row className="justify-content-center">
                    {notifications.map((notification: any) => {
                        return (
                            <Col md={3} key={notification.id}>
                                <div key={notification.id} className="notification-item p-3 mb-2 rounded bg-dark">
                                    <p className="mb-1" dangerouslySetInnerHTML={{ __html: notification.content }}></p>
                                    <p className="mb-2" style={{ fontSize: '0.8rem', textAlign: "right" }}><DateTime date={notification.createdAt} /></p>
                                    <div className="d-flex justify-content-end">
                                        <Button variant="primary" size="sm" className="me-2" onClick={() => handleRead(notification)}>Oznacz jako przeczytane</Button>
                                        <a href={`/teams/${notification.teamId}`}><Button variant="primary" size="sm">Przejdź do zespołu</Button></a>
                                    </div>
                                </div>
                            </Col>
                        );
                    })}
                </Row>
                <Row className="justify-content-center">
                    <Col md={12}>
                        <h2 className="text-center">Twoje zespoły</h2>
                    </Col>
                </Row>
                <Row className="justify-content-center">
                    {user && user.teams.map((team: any) => {
                        return (
                            <Col md={3} key={team.team.id}>
                                <div className="notification-item p-3 mb-2 rounded bg-dark">
                                    <p className="mb-1" style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>{team.team.name}</p>
                                    <p className="mb-1">{team.team.description}</p>
                                    <a href={`/teams/${team.team.id}`}><Button variant="primary" size="sm">Przejdź do zespołu</Button></a>
                                </div>
                            </Col>
                        );
                    })}
                </Row>
                <Row className="justify-content-center">
                    <Col md={12}>
                        <h2 className="text-center">Twoje zadania</h2>
                    </Col>
                </Row>
                <Row className="justify-content-center">
                    {user && user.Task.map((task: any) => {
                        if(task.status == "DURING") return (
                            <Col md={3} key={task.id}>
                                <div className="notification-item p-3 mb-2 rounded bg-dark">
                                    <p className="mb-1" style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>{task.title}</p>
                                    <p className="mb-1">{task.team.name} <Priority priority={task.priority} /></p>
                                    <a href={`/teams/${task.team.id}`}><Button variant="primary" size="sm">Przejdź do zespołu</Button></a>
                                </div>
                            </Col>
                        );
                    })}
                </Row>
            </Container>
        </>
    );
}

export default Home;