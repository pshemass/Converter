// Type definitions for path-case
// Project: https://github.com/blakeembrey/path-case
// Definitions by: Sam Saint-Pettersen <https://github.com/stpettersens>
// Definitions: https://github.com/DefinitelyTyped/DefinitelyTyped
/// <reference path="./path-case.d.ts" />

declare module "path-case" {
	function pathCase(string: string, locale?: string): string;
	export = pathCase;
}
