import webstomp from 'webstomp-client';
import SockJS from 'sockjs-client';

let client: any = null;

export function connect(onMessage: (msg: any) => void) {
    const sock = new SockJS('/ws');
    client = webstomp.over(sock);

    client.connect({}, () => {
        console.log('Połączono z WebSocket przez SockJS');
        client?.subscribe('/topic/messages', (message: any) => {
            if (message.body) {
                onMessage(JSON.parse(message.body));
            }
        });
    });
}

export function sendMessage(userId: number, teamId: number, content: string) {
    if (client && client.connected) {
        client.send(
            '/app/chat',
            JSON.stringify({ userId, teamId, content }),
            {}
        );
    }
}

export function disconnect() {
    if (client && client.connected) {
        client.disconnect(() => {
            console.log("WebSocket rozłączony.");
        });
        client = null;
    }
}
