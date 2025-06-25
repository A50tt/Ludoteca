export interface StatusResponse {
    status: string; // status of the response
    message?: string; // Message with details
    extendedMessage?: string; // Optional extended message for more details
}