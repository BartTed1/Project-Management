import React, { useEffect, useRef, useState } from 'react';
import { connect, sendMessage, disconnect } from '../chat';

type ChatMessage = {
    userId: number;
    teamId: number;
    content: string;
};

interface Props {
    userId: number;
    teamId: number;
}

export default function ChatBox({ userId, teamId }: Props) {
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [message, setMessage] = useState('');
    const bottomRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        connect((msg: ChatMessage) => setMessages(prev => [...prev, msg]));
        return () => {
            disconnect();
            setMessages([]);
        };
    }, []);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    const handleSend = () => {
        if (!userId || !message.trim()) {
            alert("Brak zalogowanego użytkownika lub wiadomości");
            return;
        }
        sendMessage(userId, teamId, message);
        setMessage('');
    };

    const handleInputKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') handleSend();
    };

    return (
        <div>
            <div style={{
                height: 300,
                border: "1px solid #aaa",
                marginBottom: 10,
                overflowY: "auto",
                background: "#222",
                color: "#fff",
                borderRadius: 6,
                padding: 8
            }}>
                {messages.length === 0 && (
                    <div style={{ color: "#888" }}>Brak wiadomości na czacie.</div>
                )}
                {messages.map((msg, i) => (
                    <div key={i}>
                        <b style={{ color: "#78d" }}>Użytkownik {msg.userId}:</b> {msg.content}
                    </div>
                ))}
                <div ref={bottomRef} />
            </div>
            <div style={{ display: "flex", gap: 8 }}>
                <input
                    value={message}
                    onChange={e => setMessage(e.target.value)}
                    onKeyDown={handleInputKeyDown}
                    placeholder="Napisz wiadomość..."
                    style={{
                        flex: 1,
                        padding: 8,
                        borderRadius: 4,
                        border: "1px solid #aaa"
                    }}
                />
                <button onClick={handleSend} style={{
                    padding: "8px 16px",
                    borderRadius: 4,
                    background: "#4682ea",
                    color: "#fff",
                    border: "none"
                }}>
                    Wyślij
                </button>
            </div>
        </div>
    );
}

