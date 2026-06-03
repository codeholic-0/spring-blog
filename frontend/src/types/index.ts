export interface PostRequest {
	title: string;
	content: string;
	author: string;
}

export interface PostResponse {
	id: number;
	title: string;
	content: string;
	author: string;
	createdAt: string;
	updatedAt: string;
}

export interface CommentRequest {
	body: string;
	author: string;
}

export interface CommentResponse {
	id: number;
	body: string;
	author: string;
	createdAt: string;
}
