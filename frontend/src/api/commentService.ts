import type { CommentRequest, CommentResponse } from "../types";

export async function getCommentsByPostId(
	postId: number,
): Promise<CommentResponse[]> {
	const res = await fetch(`/api/posts/${postId}/comments`);
	if (!res.ok) throw new Error("Failed to fetch comments");
	return res.json();
}

export async function createComment(
	postId: number,
	data: CommentRequest,
): Promise<CommentResponse> {
	const res = await fetch(`/api/posts/${postId}/comments`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(data),
	});
	if (!res.ok) throw new Error("Failed to create comment");
	return res.json();
}

export async function deleteComment(
	postId: number,
	commentId: number,
): Promise<void> {
	const res = await fetch(`/api/posts/${postId}/comments/${commentId}`, {
		method: "DELETE",
	});
	if (!res.ok) throw new Error("Failed to delete comment");
}
