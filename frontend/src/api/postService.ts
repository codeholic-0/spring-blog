import type { PostRequest, PostResponse } from "../types";

const BASE = "/api/posts";

export async function getAllPosts(): Promise<PostResponse[]> {
	const res = await fetch(BASE);
	if (!res.ok) throw new Error("Failed to fetch posts");
	return res.json();
}

export async function getPostById(id: number): Promise<PostResponse> {
	const res = await fetch(`${BASE}/${id}`);
	if (!res.ok) throw new Error("Post not found");
	return res.json();
}

export async function createPost(data: PostRequest): Promise<PostResponse> {
	const res = await fetch(BASE, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(data),
	});
	if (!res.ok) throw new Error("Failed to create post");
	return res.json();
}

export async function updatePost(
	id: number,
	data: PostRequest,
): Promise<PostResponse> {
	const res = await fetch(`${BASE}/${id}`, {
		method: "PUT",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(data),
	});
	if (!res.ok) throw new Error("Failed to update post");
	return res.json();
}

export async function deletePost(id: number): Promise<void> {
	const res = await fetch(`${BASE}/${id}`, { method: "DELETE" });
	if (!res.ok) throw new Error("Failed to delete post");
}
