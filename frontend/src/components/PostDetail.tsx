import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { deletePost, getPostById } from "../api/postService";
import {
	getCommentsByPostId,
	createComment,
	deleteComment,
} from "../api/commentService";
import type { PostResponse, CommentResponse } from "../types";

export default function PostDetail() {
	const { id } = useParams<{ id: string }>();
	const navigate = useNavigate();
	const postId = Number(id);

	const [post, setPost] = useState<PostResponse | null>(null);
	const [comments, setComments] = useState<CommentResponse[]>([]);
	const [author, setAuthor] = useState("");
	const [body, setBody] = useState("");

	useEffect(() => {
		getPostById(postId).then(setPost);
		getCommentsByPostId(postId).then(setComments);
	}, [postId]);

	async function handleDelete() {
		await deletePost(postId);
		navigate("/");
	}

	async function handleComment(e: React.FormEvent) {
		e.preventDefault();
		const created = await createComment(postId, { author, body });
		setComments((prev) => [...prev, created]);
		setAuthor("");
		setBody("");
	}

	async function handleDeleteComment(commentId: number) {
		await deleteComment(postId, commentId);
		setComments((prev) => prev.filter((c) => c.id !== commentId));
	}

	if (!post) return <p className="text-gray-500">Loading...</p>;

	return (
		<div className="space-y-6">
			{/* Post card */}
			<div className="bg-white p-6 rounded shadow">
				<h1 className="text-2xl font-bold">{post.title}</h1>
				<p className="text-sm text-gray-500">
					by {post.author} —{" "}
					{new Date(post.createdAt).toLocaleDateString()}
				</p>
				<p className="mt-4 whitespace-pre-wrap">{post.content}</p>
				<div className="mt-4 flex gap-2">
					<Link
						to={`/posts/${post.id}/edit`}
						className="bg-yellow-500 text-white px-3 py-1 rounded text-sm hover:bg-yellow-600"
					>
						Edit
					</Link>
					<button
						onClick={handleDelete}
						className="bg-red-600 text-white px-3 py-1 rounded text-sm hover:bg-red-700"
					>
						Delete
					</button>
				</div>
			</div>

			{/* Comments section */}
			<div className="bg-white p-6 rounded shadow">
				<h2 className="text-xl font-semibold mb-4">Comments</h2>

				{comments.length === 0 && (
					<p className="text-gray-500 mb-4">No comments yet.</p>
				)}

				<div className="space-y-3 mb-6">
					{comments.map((c) => (
						<div
							key={c.id}
							className="border-b pb-2 flex justify-between"
						>
							<div>
								<p className="text-sm font-medium">
									{c.author}
								</p>
								<p className="text-gray-700">{c.body}</p>
							</div>
							<button
								onClick={() => handleDeleteComment(c.id)}
								className="text-red-500 text-xs hover:underline self-start"
							>
								Delete
							</button>
						</div>
					))}
				</div>

				<form onSubmit={handleComment} className="space-y-3">
					<input
						value={author}
						onChange={(e) => setAuthor(e.target.value)}
						placeholder="Your name"
						required
						className="w-full border rounded px-3 py-2"
					/>
					<textarea
						value={body}
						onChange={(e) => setBody(e.target.value)}
						placeholder="Write a comment..."
						required
						rows={3}
						className="w-full border rounded px-3 py-2"
					/>
					<button
						type="submit"
						className="bg-blue-600 text-white px-4 py-2 rounded text-sm hover:bg-blue-700"
					>
						Post Comment
					</button>
				</form>
			</div>
		</div>
	);
}
