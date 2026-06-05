import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createPost, getPostById, updatePost } from "../api/postService";

export default function PostForm() {
	const { id } = useParams();
	const navigate = useNavigate();
	const isEdit = Boolean(id);

	const [title, setTitle] = useState("");
	const [content, setContent] = useState("");
	const [author, setAuthor] = useState("");

	useEffect(() => {
		if (!id) return;
		getPostById(Number(id)).then((post) => {
			setTitle(post.title);
			setContent(post.content);
			setAuthor(post.author);
		});
	}, [id]);

	async function handleSubmit(e: React.FormEvent) {
		e.preventDefault();
		const data = { title, content, author };

		if (isEdit) {
			await updatePost(Number(id), data);
		} else {
			await createPost(data);
		}

		navigate("/");
	}

	return (
		<form onSubmit={handleSubmit} className="max-w-lg mx-auto space-y-4">
			<h1 className="text-2xl font-bold">
				{isEdit ? "Edit Post" : "New Post"}
			</h1>

			<div>
				<label className="block text-sm font-medium">Author</label>
				<input
					value={author}
					onChange={(e) => setAuthor(e.target.value)}
					required
					className="w-full border rounded px-3 py-2"
				/>
			</div>

			<div>
				<label className="block text-sm font-medium">Title</label>
				<input
					value={title}
					onChange={(e) => setTitle(e.target.value)}
					required
					className="w-full border rounded px-3 py-2"
				/>
			</div>

			<div>
				<label className="block text-sm font-medium">Content</label>
				<textarea
					value={content}
					onChange={(e) => setContent(e.target.value)}
					required
					rows={6}
					className="w-full border rounded px-3 py-2"
				/>
			</div>

			<button
				type="submit"
				className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
			>
				{isEdit ? "Update" : "Create"}
			</button>
		</form>
	);
}
