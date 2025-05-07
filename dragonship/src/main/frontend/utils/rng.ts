/**
 * Create a deterministic, seeded PRNG function.
 *
 * Internally this uses:
 * 1. An FNV-1a 32-bit hash of the seed string to produce an initial state.
 * 2. A Mulberry32–style mixing of that state on each invocation.
 *
 * @example
 * const rng = newRng("hello");
 * console.log(rng()); // always the same “random” number in [0,1)
 * console.log(rng()); // again, deterministic next value
 *
 * @param seed
 *   An arbitrary string. Different seeds → completely different sequences.
 *
 * @returns
 *   A no-argument function which, when called, returns a pseudorandom
 *   32-bit unsigned value normalized into the half-open interval [0, 1).
 *   That is, it can be 0 (inclusive) up to but never 1 (exclusive).
 */
export function newRng(seed: string): () => number {
    // rng is named here because I want it to be called that dammit
    // noinspection UnnecessaryLocalVariableJS
    const rng = () => {
        const seedStr = seed;

        // 1) FNV-1a 32-bit hash of the seed string
        let h = 2166136261 >>> 0;
        for (let i = 0; i < seedStr.length; i++) {
            h ^= seedStr.charCodeAt(i);
            h = Math.imul(h, 16777619) >>> 0;
        }

        // 2) Mulberry32-style mixing
        let t = h + 0x6D2B79F5;
        t = Math.imul(t ^ (t >>> 15), t | 1);
        t ^= t + Math.imul(t ^ (t >>> 7), t | 61);

        // 3) Normalize to [0,1):
        //    (t ^ (t >>> 14)) is a 32-bit unsigned int 0…0xFFFFFFFF
        //    dividing by 2**32 (4294967296) yields [0,1)
        // noinspection UnnecessaryLocalVariableJS
        const rand = ((t ^ (t >>> 14)) >>> 0) / 4294967296;
        return rand;
    };

    return rng;
}



