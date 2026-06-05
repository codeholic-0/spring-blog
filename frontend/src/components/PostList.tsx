import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAllPosts } from "../api/postService";
import type { PostResponse } from "../types";

export default function PostList() {
	const [posts, setPosts] = useState<PostResponse[]>([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		getAllPosts()
			.then(setPosts)
			.finally(() => setLoading(false));
	}, []);

	if (loading) return <p className="text-gray-500">Loading posts...</p>;
	if (posts.length === 0)
		return <p className="text-gray-500">No posts yet.</p>;

	return (
		<div className="space-y-4">
			{posts.map((post) => (
				<div key={post.id} className="bg-white p-4 rounded shadow">
					<Link
						to={`/posts/${post.id}`}
						className="text-lg font-semibold text-blue-600 hover:underline"
					>
						{post.title}
					</Link>
					<p className="text-sm text-gray-500 mt-1">
						by {post.author} —{" "}
						{new Date(post.createdAt).toLocaleDateString()}
					</p>
				</div>
			))}
		</div>
	);
}
