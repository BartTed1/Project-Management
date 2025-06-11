import { useEffect, useState } from "react";
import { getNotifications, readNotification } from "../connection";
import { Container, Pagination, Table } from "react-bootstrap";
import DateTime from "../components/DateTime";

const Notifications = () => {
    const [notifications, setNotifications] = useState(false as any);
    const urlParams = new URLSearchParams(window.location.search);
    const page = parseInt(urlParams.get('page') || '1', 10);
    const pageSize = parseInt(urlParams.get('pageSize') || '10', 10);

    useEffect(() => {
        const fetchNotifications = async () => {
            const res = await getNotifications(pageSize, page);
            if (res) {
                setNotifications(res);
            }else{
                alert("Failed to fetch notifications");
            }
        }
        fetchNotifications();
    }, []);

    const handlePageChange = (pageNumber: number) => {
        urlParams.set('page', pageNumber.toString());
        window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
        location.reload();
    };

    const handleRead = async (notification : any) => {
        if(await readNotification(notification.id)) {
          const data = await getNotifications(pageSize, page);
          setNotifications(data);
        }
    }

    if (!notifications) return <div>Loading...</div>;

    return (
        <>
            <Container>
                <h1 className='my-4'>Powiadomienia</h1>
                <Table striped bordered hover variant='dark'>
                    <thead className='text-center'>
                        <tr>
                            <th style={{ width: '100px' }}>#</th>
                            <th style={{ width: '100px' }}>Status</th>
                            <th>Opis</th>
                            <th style={{ width: '180px' }}>Data powiadomienia</th>
                            <th style={{ width: '280px' }}>Akcje</th>
                        </tr>
                    </thead>
                    <tbody>
                        {notifications.notifications.map((notification: any) => {
                            return <tr key={notification.id}>
                                <td>{notification.id}</td>
                                <td>{notification.read ? 'Przeczytane' : 'Nieprzeczytane'}</td>
                                <td dangerouslySetInnerHTML={{__html: notification.content}}></td>
                                <td><DateTime date={notification.createdAt} /></td>
                                <td>
                                    {!notification.read && (<button className='btn btn-sm btn-primary mr-2' style={{ marginRight: '0.5rem' }} onClick={() => handleRead(notification)}>Oznacz jako przeczytane</button>)}
                                    <a href={`/teams/${notification.teamId}`} className='btn btn-sm btn-primary'>Przejdź do zespołu</a>
                                </td>
                            </tr>
                        })}
                    </tbody>
                </Table>
                {notifications.pageTotal > 1 &&
                    <Pagination className="justify-content-center">
                        <Pagination.First onClick={() => handlePageChange(1)} disabled={page === 1} />
                        <Pagination.Prev onClick={() => handlePageChange(page - 1)} disabled={page === 1} />
                        {[...Array(notifications.pageTotal)].map((_, index) => (
                        <Pagination.Item
                            key={index + 1}
                            active={index + 1 === page}
                            onClick={() => handlePageChange(index + 1)}
                            className="bg-dark text-white"
                        >
                            {index + 1}
                        </Pagination.Item>
                        ))}
                        <Pagination.Next onClick={() => handlePageChange(page + 1)} disabled={page === notifications.pageTotal} />
                        <Pagination.Last onClick={() => handlePageChange(notifications.pageTotal)} disabled={page === notifications.pageTotal} />
                    </Pagination>
                }
            </Container>
        </>
    );
}

export default Notifications;