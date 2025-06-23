import React, { useEffect, useState } from "react";
import { connect, sendMessage } from "../chat";

interface Message {
    userId: number;
    teamId: number;
    content: string;
    timestamp?: string;
}

const Chat: React.FC<{ userId: number; teamId: number }> = ({ userId, teamId }) => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState("");

    useEffect(() => {
        connect((message: Message) => {
            console.log("ChatBox: ODEBRANO wiadomość:", message); // LOG
            setMessages(prev => [...prev, message]);
        });
    }, []);

    const handleSend = () => {
        if (input.trim()) {
            sendMessage(userId, teamId, input.trim());
            setInput('');
        }
    };

    return (
        <div style={{ padding: 20 }}>
            <h3>💬 Czat zespołu</h3>
            <div style={{ border: "1px solid #ccc", minHeight: 200, maxHeight: 300, overflowY: "auto", background: "#181d22", color: "white", marginBottom: 12 }}>
                {messages.map((msg, idx) => (
                    <div key={idx}>
                        <strong>{msg.userId}</strong>: {msg.content}
                        <div style={{ fontSize: "0.8em", color: "#888" }}>{msg.timestamp}</div>
                    </div>
                ))}
            </div>
            <input
                value={input}
                onChange={e => setInput(e.target.value)}
                onKeyDown={e => e.key === "Enter" && handleSend()}
                placeholder="Napisz wiadomość..."
                style={{ width: "70%", marginRight: 10, padding: 5 }}
            />
            <button onClick={handleSend}>Wyślij</button>
        </div>
    );
};

export default Chat;
