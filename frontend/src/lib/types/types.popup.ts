export interface OverlayOutput {
	firstScore: number;
	secondScore: number;
	date: string;
}

export interface OverlayInput {
	message: string;
	firstName: string;
	secondName: string;
	date?: string;
}
